import stdlib;

import additive3pp;

domain pd_a3p additive3pp;

void debugAutomaton(pd_a3p bool[[2]] matrix) {
  uint64 rows = shape(matrix)[0];
  uint64 nstates = shape(matrix)[1];

  uint64 nlabels = (rows - 2) / nstates;
  print("******************* Automata *******************");
  print("rows: ", rows);

  uint64 i;
  uint64 j;
  uint64 l;

  for (i=0; i<nstates; i=i+1) {
    bool acc = declassify(matrix[rows-2,i]);
    bool start = declassify(matrix[rows-1,i]);
    print ("State ", i, " acc ", acc, " start ", start);
    for (l=0; l<nlabels; l=l+1) {
      for (j=0; j<nstates; j=j+1) {
	if (declassify(matrix[(i*nlabels)+l,j]))
	  print ("   ", i, " ---", l, "---> ", j);
      }
    }
  }
}

template<domain D : additive3pp>
void putColumn(string colname, D uint64 val) {
    __syscall("additive3pp::cademo_putcolumn_uint64", __domainid(D), __cref colname, val);
}

template<domain D : additive3pp>
void putColumn(string colname, D uint64 [[1]] val) {
    __syscall("additive3pp::cademo_putcolumn_uint64", __domainid(D), __cref colname, val);
}

void main() {
  print("Loading Automaton");
  
  pd_a3p bool[[1]] val = argument("input");
  pd_a3p uint64 sec_nstateA = argument("nstates");
  pd_a3p uint64 sec_nlabelsA = argument("nlabels");
  pd_a3p uint64 sec_automaton_idx = argument("automaton_idx");
  uint64 automaton_idx = declassify(sec_automaton_idx);

  string tbl_name = "automatonA";
  if (automaton_idx == 0) {
    tbl_name = "automatonA";
  }
  else {
    tbl_name = "automatonB";
  }

  uint64 nstateA = declassify(sec_nstateA);
  uint64 nlabelsA = declassify(sec_nlabelsA);


  print("nstateA", nstateA);
  print("nlabelsA", nlabelsA);

  pd_a3p uint64[[1]] dbVal = (uint64)val;
  putColumn(tbl_name, dbVal);
  putColumn(tbl_name+"-nstate", sec_nstateA);
  putColumn(tbl_name+"-nlabels", sec_nlabelsA);



  pd_a3p bool[[1]] loadedVal = (bool)dbVal;

  pd_a3p bool[[2]] statesA(nstateA*nlabelsA+2, nstateA) = reshape(loadedVal, nstateA*nlabelsA+2, nstateA);

  debugAutomaton(statesA);
} 
