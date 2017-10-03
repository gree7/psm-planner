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

pd_a3p  bool[[2]] ndet_mult(pd_a3p  bool[[2]] ndet_automataA, pd_a3p  bool[[2]] ndet_automataB) {
  uint64 ndet_nstateA = shape(ndet_automataA)[1];
  uint64 ndet_nstateB = shape(ndet_automataB)[1];
  uint64 rows = shape(ndet_automataA)[0];
  uint64 nlabels = (rows - 2) / ndet_nstateA;

  pd_a3p bool[[1]] ndet_acceptA(ndet_nstateA) = ndet_automataA[ndet_nstateA*nlabels,:];
  pd_a3p bool[[1]] ndet_startA(ndet_nstateA) = ndet_automataA[ndet_nstateA*nlabels+1,:];

  pd_a3p bool[[1]] ndet_acceptB(ndet_nstateB) = ndet_automataB[ndet_nstateB*nlabels,:];
  pd_a3p bool[[1]] ndet_startB(ndet_nstateB) = ndet_automataB[ndet_nstateB*nlabels+1,:];

  uint64 cState = ndet_nstateA * ndet_nstateB;
  pd_a3p bool[[2]] res_automata(cState*nlabels+2,cState);
  pd_a3p bool[[2]] tmpAutoA(cState*nlabels+2,cState);
  pd_a3p bool[[2]] tmpAutoB(cState*nlabels+2,cState);

  uint64 i;
  for (i = 0; i < ndet_nstateA; i = i + 1) {
    uint64 j;
    for (j = 0; j < ndet_nstateB; j = j + 1) {
      uint64 l;
      for (l = 0; l < nlabels; l = l + 1) {
	uint64 i1;
	for (i1 = 0; i1 < ndet_nstateA; i1 = i1 + 1) {
	  uint64 j1;
	  for (j1 = 0; j1 < ndet_nstateB; j1 = j1 + 1) {
	    tmpAutoA[(i*ndet_nstateB + j) * nlabels + l,(i1*ndet_nstateB + j1)] = ndet_automataA[i*nlabels + l,i1];
	    tmpAutoB[(i*ndet_nstateB + j) * nlabels + l,(i1*ndet_nstateB + j1)] = ndet_automataB[j*nlabels + l,j1];
	  }
	}
      }
      tmpAutoA[cState * nlabels,i*ndet_nstateB + j] = ndet_acceptA[i];
      tmpAutoB[cState * nlabels,i*ndet_nstateB + j] = ndet_acceptB[j];
      tmpAutoA[cState * nlabels + 1,i*ndet_nstateB + j] = ndet_startA[i];
      tmpAutoB[cState * nlabels + 1,i*ndet_nstateB + j] = ndet_startB[j];
    }
  }

  res_automata = tmpAutoA && tmpAutoB;
  return res_automata;
}

pd_a3p  bool[[2]] remove_no_usefoul(pd_a3p  bool[[2]] ndet_automata) {
  uint64 ndet_nstate = shape(ndet_automata)[1];
  uint64 rows = shape(ndet_automata)[0];
  uint64 nlabels = (rows - 2) / ndet_nstate;

  pd_a3p bool[[1]] ndet_accept(ndet_nstate) = ndet_automata[ndet_nstate*nlabels,:];
  pd_a3p bool[[1]] ndet_start(ndet_nstate) = ndet_automata[ndet_nstate*nlabels+1,:];

  pd_a3p bool[[2]] res_automata(ndet_nstate*nlabels+2,ndet_nstate);
  pd_a3p bool[[1]] res_accept(ndet_nstate);
  pd_a3p bool[[1]] res_start(ndet_nstate);
    
  pd_a3p bool[[2]] M(ndet_nstate,ndet_nstate);

  uint64 l;
  for (l = 0; l < nlabels; l = l + 1) {
    pd_a3p bool[[2]] M1(ndet_nstate,ndet_nstate);
    uint64 i;
    for (i = 0; i < ndet_nstate; i = i + 1) {
      uint64 j;
      for (j = 0; j < ndet_nstate; j = j + 1) {
	M1[i,j] = ndet_automata[i*nlabels+l,j];
      }
      M1[i,i] = true;
    }
    pd_a3p bool[[2]] M2(ndet_nstate,ndet_nstate) = M || M1;
    M = M2;
  }

  pd_a3p bool[[1]] mask(ndet_nstate) = ndet_accept;
  print("Matrix Done");
  print("Closure....");

  // Tansitive closure
  uint64 k;
  for (k = 0; k < ndet_nstate; k = k + 1) {
    pd_a3p bool[[2]] mask_matrix(ndet_nstate,ndet_nstate);
    uint64 i;
    for (i = 0; i < ndet_nstate; i = i + 1) {
      mask_matrix[i,:] = mask;
    }
    pd_a3p bool[[2]] target_matrix(ndet_nstate,ndet_nstate);
    target_matrix = M && mask_matrix;

    pd_a3p uint64[[1]] target_num_vec(ndet_nstate);
    for (i = 0; i < ndet_nstate; i = i + 1) {
      pd_a3p bool[[1]] targets(ndet_nstate) = target_matrix[i,:];
      pd_a3p uint64 targetsNum = sum(targets);
      target_num_vec[i] = targetsNum;
    }

    pd_a3p uint64[[1]] zero_vec(ndet_nstate);
    mask = (target_num_vec > zero_vec);
  }

  print("Closure done");

  uint64 i;
  pd_a3p bool[[2]] arc_mask1(ndet_nstate*nlabels+2,ndet_nstate);
  pd_a3p bool[[2]] arc_mask2(ndet_nstate*nlabels+2,ndet_nstate);

  for (i = 0; i < ndet_nstate; i = i + 1) {
    for (l = 0; l < nlabels; l = l + 1) {
      uint64 j;
      for (j = 0; j < ndet_nstate; j = j + 1) {
	arc_mask1[i*nlabels+l,j] = mask[i];
	arc_mask2[i*nlabels+l,j] = mask[j];
      }
    }
  }

  pd_a3p bool[[2]] arcs(ndet_nstate*nlabels+2,ndet_nstate);

  arcs = ndet_automata && arc_mask1;
  res_automata = arcs && arc_mask2;

  res_accept = ndet_accept && mask;
  res_start = ndet_start && mask;

  print("Usefoul Done");

  res_automata[ndet_nstate * nlabels,:] = res_accept;
  res_automata[ndet_nstate * nlabels + 1,:] = res_start;

  return res_automata;
}

void ndet_ps(pd_a3p bool[[2]] ndet_automata, uint64 nstate) {
  uint64 ndet_nstate = shape(ndet_automata)[1];
  uint64 rows = shape(ndet_automata)[0];
  uint64 nlabels = (rows - 2) / ndet_nstate;

  pd_a3p bool[[1]] ndet_accept(ndet_nstate) = ndet_automata[ndet_nstate*nlabels,:];
  pd_a3p bool[[1]] ndet_start(ndet_nstate) = ndet_automata[ndet_nstate*nlabels+1,:];

  pd_a3p uint64[[2]] states(nstate,nlabels);
  pd_a3p bool[[1]] acc(nstate);
  pd_a3p bool[[2]] statesData(nstate,ndet_nstate);
  pd_a3p bool[[1]] statesValid(nstate);

  statesData[0,:] = ndet_start;
  statesValid[0] = true;
  
  print("Start Powerset");
  uint64 cState;
  for (cState = 0; cState < nstate; cState = cState + 1) {
    print("Det state ", cState);
    pd_a3p bool[[1]] sData(ndet_nstate) = statesData[cState,:];
    uint64 l;
    for (l = 0; l < nlabels; l = l + 1) {
      print ("  label ", l);
      pd_a3p bool[[1]] targets(ndet_nstate);
      uint64 i;
      for (i = 0; i < ndet_nstate; i = i + 1) {
	pd_a3p bool isStateInData = sData[i];
	uint64 j;
	pd_a3p bool[[1]] isStateInData_vec(ndet_nstate);
	for (j = 0; j < ndet_nstate; j = j + 1) { isStateInData_vec[j] = isStateInData; }
	
	pd_a3p bool[[1]] ndet_target(ndet_nstate);
	pd_a3p bool[[1]] old_target1(ndet_nstate);
	pd_a3p bool[[1]] old_target2(ndet_nstate);
	ndet_target = ndet_automata[i*nlabels + l,:];
	old_target1 = (isStateInData_vec && ndet_target);
	old_target2 = (targets || old_target1);
	targets = old_target2;
      }
      //print("targets ", cState, " ", l);
      //printVector(declassify(targets));

      pd_a3p uint64 idx = 0;
      pd_a3p bool idxFound = false;
      uint64 k;

      for (k = 0; k < nstate; k = k + 1) {
	uint64 j;
	pd_a3p bool equal = true;
	pd_a3p bool[[1]] curr_row(ndet_nstate) = statesData[k,:];
	pd_a3p bool[[1]] equals(ndet_nstate) = (curr_row == targets);
        pd_a3p uint64 equalsNum = sum(equals);
	equal = (equalsNum == ndet_nstate);

	pd_a3p bool c1 = (!idxFound) && equal;
	pd_a3p bool c2 = (!idxFound) && (!statesValid[k]);
	pd_a3p bool not_selected = (!c1)&&(!c2);
	idxFound = idxFound || (!not_selected);

        pd_a3p uint64 tmpBool = (uint64)not_selected;
	idx = idx*tmpBool+k*(1-tmpBool);
	statesValid[k] = (statesValid[k]&&not_selected)||(!not_selected);

	pd_a3p bool not_c2 = !c2;
	pd_a3p bool[[1]] c2_vec(ndet_nstate);
	pd_a3p bool[[1]] not_c2_vec(ndet_nstate);
	for (j = 0; j < ndet_nstate; j = j + 1) { c2_vec[j] = c2; }
	for (j = 0; j < ndet_nstate; j = j + 1) { not_c2_vec[j] = not_c2; }

	pd_a3p bool[[1]] old_state_data(ndet_nstate);
	pd_a3p bool[[1]] old_state_data1(ndet_nstate);
	pd_a3p bool[[1]] old_state_data2(ndet_nstate);
	pd_a3p bool[[1]] old_state_data3(ndet_nstate);
	old_state_data = statesData[k,:];
	old_state_data1 = (old_state_data && not_c2_vec);
	old_state_data2 = (targets && c2_vec);
	old_state_data3 = (old_state_data1 || old_state_data2);
	statesData[k,:] = old_state_data3;
      }
      states[cState,l] = idx;
    }
    uint64 i;
    for (i = 0; i < ndet_nstate; i = i + 1) {
      acc[cState] = acc[cState] || (statesData[cState,i] && ndet_accept[i]);  
    }
  }
  
  print("acc");
  printVector(declassify(acc));
  print("states");
  printMatrix(declassify(states));

  // Publish the results
  publish("acc", declassify(acc));
  publish("states", declassify(states));
}

template<domain D : additive3pp>
D uint64 [[1]] getColumn(string colname) {
    uint32 colSize = 0;
    __syscall("additive3pp::cademo_getcolumnsize_uint64", __domainid(D), __cref colname, __ref colSize);

    D uint64 [[1]] rv((uint64)colSize);
    if (colSize != 0)
        __syscall("additive3pp::cademo_getcolumn_uint64", __domainid(D), __cref colname, rv);

    return rv;
}

template<domain D : additive3pp>
D uint64 getValue(string colname) {
    uint32 colSize = 0;
    __syscall("additive3pp::cademo_getcolumnsize_uint64", __domainid(D), __cref colname, __ref colSize);

    D uint64 [[1]] rv((uint64)colSize);
    if (colSize != 0)
        __syscall("additive3pp::cademo_getcolumn_uint64", __domainid(D), __cref colname, rv);

    return rv[0];
}


void main() {
  /*
  uint64 nstateA = 4;
  uint64 nlabelsA = 2;

  pd_a3p bool[[2]] statesA(nstateA*nlabelsA+2, nstateA);
  pd_a3p bool[[1]] accA(nstateA);
  pd_a3p bool[[1]] startsA(nstateA);

  // a(a|b)
  accA[0] = true;
  startsA[0] = false;

  statesA[1*nlabelsA+0,0] = true;
  statesA[1*nlabelsA+1,0] = true;
  accA[1] = false;
  startsA[1] = false;

  statesA[2*nlabelsA+0,1] = true;
  accA[2] = false;
  startsA[2] = true;

  statesA[nstateA*nlabelsA,:] = accA;
  statesA[nstateA*nlabelsA+1,:] = startsA;
  */

  string tbl_name = "automatonA";
  tbl_name = tbl_name + "-nstate";
    

  pd_a3p uint64[[1]] dbVal = getColumn("automatonA");
  pd_a3p uint64 sec_nstateA = getValue("automatonA-nstate");
  pd_a3p uint64 sec_nlabelsA = getValue("automatonA-nlabels");
  pd_a3p bool[[1]] loadedVal = (bool)dbVal;

  uint64 nstateA = declassify(sec_nstateA);
  uint64 nlabelsA = declassify(sec_nlabelsA);
  pd_a3p bool[[2]] statesA(nstateA*nlabelsA+2, nstateA) = reshape(loadedVal, nstateA*nlabelsA+2, nstateA);

  debugAutomaton(statesA);

  /*
  uint64 nstateB = 8;
  uint64 nlabelsB = nlabelsA;

  pd_a3p bool[[2]] statesB(nstateB*nlabelsB+2, nstateB);
  pd_a3p bool[[1]] accB(nstateB);
  pd_a3p bool[[1]] startsB(nstateB);

  // a(b|a|ba)
  accB[0] = true;
  startsB[0] = false;

  statesB[1*nlabelsB+0,0] = true;
  accB[1] = false;
  startsB[1] = false;

  statesB[2*nlabelsB+0,1] = true;
  statesB[2*nlabelsB+0,3] = true;
  statesB[2*nlabelsB+0,4] = true;
  accB[2] = false;
  startsB[2] = true;

  statesB[3*nlabelsB+1,0] = true;
  accB[3] = false;
  startsB[3] = false;

  statesB[4*nlabelsB+1,1] = true;
  accB[4] = false;
  startsB[4] = false;

  statesB[nstateB*nlabelsB,:] = accB;
  statesB[nstateB*nlabelsB+1,:] = startsB;
  */


  dbVal = getColumn("automatonB");
  pd_a3p uint64 sec_nstateB = getValue("automatonB-nstate");
  pd_a3p uint64 sec_nlabelsB = getValue("automatonB-nlabels");
  loadedVal = (bool)dbVal;

  uint64 nstateB = declassify(sec_nstateB);
  uint64 nlabelsB = declassify(sec_nlabelsB);
  pd_a3p bool[[2]] statesB(nstateB*nlabelsB+2, nstateB) = reshape(loadedVal, nstateB*nlabelsB+2, nstateB);

  debugAutomaton(statesB);

  print("Initialization done");

  pd_a3p bool[[2]] statesMul = ndet_mult(statesA, statesB);
  print ("mult Done");
  // debugAutomaton(statesMul);


  pd_a3p bool[[2]] statesMulUse = remove_no_usefoul(statesMul);
  print ("trim Done");
  //debugAutomaton(statesMulUse);

  ndet_ps(statesMulUse, (uint64)8);
  print ("PS completed");
  

  // Publish the results
  publish("nstateA", nstateA + 1);
}
