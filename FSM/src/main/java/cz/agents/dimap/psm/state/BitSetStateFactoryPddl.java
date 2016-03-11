package cz.agents.dimap.psm.state;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.tools.Pair;
import cz.agents.dimap.tools.pddl.PddlCondition;
import cz.agents.dimap.tools.pddl.PddlName;
import cz.agents.dimap.tools.pddl.PddlProblem;
import cz.agents.dimap.tools.pddl.PddlTerm;
import cz.agents.dimap.tools.pddl.parser.PddlList;
import cz.agents.dimap.tools.pddl.types.PddlPredicateType;
import cz.agents.dimap.tools.pddl.types.PddlType;

/**
 * @author honza
 *
 * In BitSetState representation each bit corresponds to one grounded predicate.
 * Formulas (ungrounded predicates) thus correspond to a continuous sequence of bits 
 * - when several bits are true then multiple grounded facts are true in the state.
 * 
 */
public class BitSetStateFactoryPddl implements BitSetStateFactory { 
	
	/**
	 * Index of first grounded predicate of this formula
	 */
	private final Map<PddlName, Pair<Integer, Integer>> formulaIndexes = new LinkedHashMap<>();

	/**
	 * Indexes of the PddlName of each term (terms represent dimensions of hypercube)
	 */
	private final Map<PddlName, List<Map<PddlName, Integer>>> constantIndexes = new HashMap<>();

    private ArrayList<Pair<Integer, Integer>> publicIndexes; // index of first and size
		
	public BitSetStateFactoryPddl(PddlProblem problem) {

		int index = 0; 
        publicIndexes = new ArrayList<>();
		
		Map<PddlName, Pair<Integer, Integer>> tmpFormulaIndexes = new LinkedHashMap<>();
	
		//for (Entry<PddlName, PddlMap> predicate : problem.domain.predicateTypes.entrySet() ) {
		for (PddlPredicateType predicateType : problem.domain.predicateTypes.values() ) {
		    PddlName predicateName = predicateType.predicateName;
		    
			int size = 1;
			List<Map<PddlName, Integer>> terms = new ArrayList<>();
			for (Entry<PddlName, PddlType> binding : predicateType.arguments.getBindings().entrySet()) {
				Set<PddlName> typedDomain = problem.getObjectsByType(binding.getValue());
				Map<PddlName, Integer> constants = new LinkedHashMap<PddlName, Integer>();
				int offset = 0;
				for (PddlName PddlName : typedDomain) {
					constants.put(PddlName, offset); 
					offset++;
				}
				terms.add(constants);
				size *= typedDomain.size();
			}
            tmpFormulaIndexes.put(predicateName, new Pair<Integer, Integer>(index, size));
            if (problem.isPublicPredicate(predicateName)) {
                publicIndexes.add(new Pair<Integer, Integer>(index, size));
            }
			constantIndexes.put(predicateName, terms);
			index += size;
		}

		ArrayList<PddlName> sortedFormulas = new ArrayList<>(tmpFormulaIndexes.keySet());
		Collections.sort(sortedFormulas, new Comparator<PddlName>() {
			@Override
			public int compare(PddlName o1, PddlName o2) {
				return o1.name.compareTo(o2.name);
			}
		});
		
		for (PddlName atomicFormula : sortedFormulas) {
			formulaIndexes.put(atomicFormula, tmpFormulaIndexes.get(atomicFormula));
		}
		
		addSharedPredicatesToPublicIndexes(problem.domain.sharedPredicates);
	}

	private void addSharedPredicatesToPublicIndexes(PddlList<PddlTerm> sharedPredicates) {
	    for (PddlTerm predicate : sharedPredicates.objects) {
            int index = formulaToInt(predicate, formulaIndexes, constantIndexes);
            publicIndexes.add(new Pair<Integer, Integer>(index, 1));            
        }
    }

    public BitSetState createInitState(PddlCondition init) {
        BitSetState state = createBitSetState(init.positives, formulaIndexes, constantIndexes);
        return state;
	}
	
	public MultiState createMultiState(PddlCondition condition) {
        BitSetState bitState = createBitSetState(condition.positives, formulaIndexes, constantIndexes);

        List<PddlTerm> allPredicates = new ArrayList<>(condition.positives);
        allPredicates.addAll(condition.negatives);
        BitSetState bitStateMask = createBitSetState(allPredicates, formulaIndexes, constantIndexes);
		return new MultiState(bitState, bitStateMask);		
	}

	public MultiState createPublicMultiState(PddlCondition condition) {
	    MultiState multiState = createMultiState(condition);
	    return new MultiState(projectToPublic(multiState.getBitState()), projectToPublic(multiState.getBitStateMask()));      
	}

    private BitSetState createBitSetState(List<PddlTerm> formulas,
            Map<PddlName, Pair<Integer, Integer>> formulaIndexes,
            Map<PddlName, List<Map<PddlName, Integer>>> constantIndexes) {
        BitSet state = new BitSet();
        for (PddlTerm formula : formulas) {
            int index = formulaToInt(formula, formulaIndexes, constantIndexes);
            state.set(index);
        }
        return new BitSetState(state);
    }

    public List<String> toPredicates(BitSet state) {
        List<String> predicates = new ArrayList<>(state.cardinality());
        for (int curBit = state.nextSetBit(0); curBit >= 0; curBit = state.nextSetBit(curBit+1)) {
            predicates.add(intToPredicate(curBit).toString());
        }
        return predicates;
    }

    private PddlTerm intToPredicate(int predicateNum) {
        PddlName name = null;
        int offset = -1;
        for (Entry<PddlName, Pair<Integer, Integer>> predicate : formulaIndexes.entrySet()) {
            int curIndex = predicate.getValue().getLeft();
            int curSize = predicate.getValue().getRight();
            if (predicateNum >= curIndex && predicateNum < curIndex+curSize) {
                name = predicate.getKey();
                offset = predicateNum - curIndex;
                break;
            }
        }
        if (name == null) {
            throw new IllegalArgumentException("Wrong predicate number: "+predicateNum+" -- "+formulaIndexes);
        }
        
        List<Set<PddlName>> domains = new ArrayList<>(constantIndexes.size());
        for (Map<PddlName, Integer> constants : constantIndexes.get(name)) {
            domains.add(constants.keySet());
        }
        Collections.reverse(domains);
        CartesianProduct<PddlName> cartProduct = new CartesianProduct<>(domains);
        List<PddlName> product = cartProduct.element(offset);
        Collections.reverse(product);
        return new PddlTerm(name, product);
    }

    @Override
    public String toString(State state) {
        List<String> predicates = toPredicates(state.getBitSet());
        Collections.sort(predicates);
        StringBuilder str = new StringBuilder(1000);
        str.append('{');
        for (String predicate : predicates) {
            if (str.length() > 1) {
                 str.append("|");
            }
            str.append(predicate);
        }
        str.append('}');
        return str.toString();
    }

    
    private int formulaToInt(PddlTerm formula,
            Map<PddlName, Pair<Integer, Integer>> formulaIndexes,
            Map<PddlName, List<Map<PddlName, Integer>>> constantIndexes) {

        PddlName formulaName = formula.name;
        List<PddlName> arguments = formula.arguments;
        if (!constantIndexes.containsKey(formulaName)) {
            if (arguments.isEmpty()) {
                // we have grounded formula, but not grounded indexes
                String[] splitFormula = formula.name.name.split("-");
                formulaName = new PddlName(splitFormula[0]);
                for (int i=1; i<splitFormula.length; i++) {
                    arguments.add(new PddlName(splitFormula[i]));
                }
            } else {
                throw new IllegalArgumentException("Wrong formula: " + formula + " -- " + constantIndexes);
            }
        }
        
        List<Map<PddlName, Integer>> terms = constantIndexes.get(formulaName);//findFormulaInMap(formula.name, constantIndexes);
        Iterator<Map<PddlName, Integer>> termIterator = terms.iterator();
        int size = 1;
        int index = 0; //findFormulaInMap(formula, formulaIndexes);
        for (PddlName term : arguments) {
            Map<PddlName, Integer> constants = termIterator.next();
            int offset = constants.get(term);
            size = constants.size();
            index *= size;
            index += offset;
        }
        return index + formulaIndexes.get(formulaName).getLeft();
    }

	@Override
    public MultiState projectToPublic(MultiState state) {
        return new MultiState(projectToPublic(state.getBitState()), projectToPublic(state.getBitStateMask()));
    }
	
    @Override
    public BitSetState projectToPublic(State state) {
	    BitSet bitSet = ((BitSetState) state).getBitSet();
        BitSet projection = new BitSet();
	    for (int curBit = bitSet.nextSetBit(0); curBit >= 0; curBit = bitSet.nextSetBit(curBit+1)) {
    	    for (Pair<Integer, Integer> publicIndex : publicIndexes) {
                int first = publicIndex.getLeft();
                int size = publicIndex.getRight();
                if (curBit >= first && curBit < first+size) {
                    projection.set(curBit);
                    break;
                }
            }
	    }
	    return new BitSetState(projection);
	}
}
