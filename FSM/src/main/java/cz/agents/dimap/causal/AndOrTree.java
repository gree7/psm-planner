package cz.agents.dimap.causal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cz.agents.alite.math.CartesianProduct;
import cz.agents.dimap.fsm.FiniteStateMachine;
import cz.agents.dimap.fsm.Transition;

public class AndOrTree {

    AndNode root;
    
    public AndOrTree(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNodeAction node) {
        root = new AndNode(fsm, node, new HashSet<FsmNode>());
    }

    public void toCnf() {
        while (toCnfAllNodes()) {
            while (root.simplify());
        }
    }

    private boolean toCnfAllNodes() {
        return toCnfAllNodes(root);
    }

    private boolean toCnfAllNodes(Node node) {
        for (Node child : node.children) {
            Node newChild = toCnf(child);
            if (!child.equals(newChild)) {
                node.children.remove(child);
                node.children.add(newChild);
                return true;
            }
            if (toCnfAllNodes(child)) {
                return true;
            }
        }
        return false;
    }

    private static Node toCnf(Node node) {
        if (node instanceof OrNode) {
            List<Set<Node>> domains = new ArrayList<>();
            List<Node> necessary = new ArrayList<>();
            for (Node child : node.children) {
                if (child instanceof Leaf) {
                    necessary.add(new OrNode(Arrays.asList(child), false));
                } else {
                    // child instance of AndNode (because siplify())
                    domains.add( new HashSet<>(child.children) );
                }
            }
         
            CartesianProduct<Node> product = new CartesianProduct<>(domains);
            int size = product.size();
            if (size > 0) {
                List<Node> newNodes = new ArrayList<Node>();
                for (int i=0; i<size; i++) {
                    List<Node> result = product.element(i);
                    result.addAll(necessary);
                    newNodes.add(new OrNode(result, false));
                }
                return new AndNode(newNodes);
            } else {
                return node;
            }
        }

        // no change
        return node;
    }
    
    @Override
    public String toString() {
        return root.toString();
    }
}

abstract class Node {
    List<Node> children;

    public Node(List<Node> children) {
        this.children = children;
    }

    abstract public boolean simplify();

    @Override
    public String toString() {
        return children.toString();
    }
}

class Leaf extends Node {
    FsmNodeAction label;
    public Leaf(FsmNodeAction label) {
        super(Collections.<Node>emptyList());
        this.label = label;
    }
    
    @Override
    public boolean simplify() {
        return false;
    }
    
    @Override
    public String toString() {
        return label.action.name;
    }
}

class AndNode extends Node {

    List<Boolean> isEpsilon = new ArrayList<>();
    
    public AndNode(List<Node> children) {
        super(children);
        
    }
    
    public AndNode(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode node, HashSet<FsmNode> visited) {
        super(nodesToChildren(fsm, node, visited));

        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(node)) {
            isEpsilon.add(trans.getOperator().isEpsilon());
        }
    }

    public boolean simplify() {
        for (Node node : children) {
            if (node instanceof AndNode) {
                int index = children.indexOf(node);
                children.addAll(node.children);
                boolean isEps = isEpsilon.remove(index);
                for (int i=0; i<node.children.size(); i++) {
                    isEpsilon.add(isEps);
                }
                children.remove(node);
                return true;
            } else {
                if (node.simplify()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Node> nodesToChildren(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode node, HashSet<FsmNode> visited) {
        List<Node> children = new ArrayList<>();
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(node)) {
            FsmNode toNode = trans.getToState();
            if (!visited.contains(toNode)) {
                visited.add(toNode);
                if (fsm.getInverseTransitions(toNode).isEmpty()) {
                    children.add(new Leaf((FsmNodeAction)toNode));
                } else {
                    try {
                        children.add(new OrNode(fsm, trans, visited));
                    } catch (IllegalStateException e) {
                    }
                }
                visited.remove(toNode);
            }
        }
        if (children.isEmpty()) {
            throw new IllegalStateException();
        }
        return children;
    }
    
    @Override
    public String toString() {
        return "(AND " + super.toString() + ")";
    }
}

class OrNode extends Node {

    boolean isRecyclable;
    
    public OrNode(List<Node> children, boolean isRecyclable) {
        super(children);
        this.isRecyclable = isRecyclable;
    }

    public OrNode(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, Transition<FsmNode, EpsilonTransition> trans, HashSet<FsmNode> visited) {
        super(nodesToChildren(fsm, trans.getToState(), visited));
        this.isRecyclable = trans.getOperator().isEpsilon();
    }

    @Override
    public boolean simplify() {
        for (Node node : children) {
            if (node instanceof OrNode) {
                children.addAll(node.children);
                children.remove(node);
                return true;
            } else {
                if (node.simplify()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static List<Node> nodesToChildren(FiniteStateMachine<FsmNode, EpsilonTransition> fsm, FsmNode node, HashSet<FsmNode> visited) {
        List<Node> children = new ArrayList<>();
        for (Transition<FsmNode, EpsilonTransition> trans : fsm.getInverseTransitions(node)) {
            FsmNode toNode = trans.getToState();
            if (!visited.contains(toNode)) {
                visited.add(toNode);
                if (fsm.getInverseTransitions(toNode).isEmpty()) {
                    children.add(new Leaf((FsmNodeAction)toNode));
                } else {
                    try {
                        children.add(new AndNode(fsm, toNode, visited));
                    } catch (IllegalStateException e) {
                    }
                }
                visited.remove(toNode);
            }
        }
        if (children.isEmpty()) {
            throw new IllegalStateException();
        }
        return children;
    }

    @Override
    public String toString() {
        return "(OR"+ ( isRecyclable?"+":"-" ) + super.toString() + ")";
    }
}