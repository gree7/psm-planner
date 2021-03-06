import stdlib;

import additive3pp;
import a3p_sort;

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

void minimize(pd_a3p  bool[[2]] ndet_automata) {
  uint64 ndet_nstate = shape(ndet_automata)[1];
  uint64 rows = shape(ndet_automata)[0];
  uint64 nlabels = (rows - 2) / ndet_nstate;

  uint64 nstate_2power = 2;
  while (nstate_2power < ndet_nstate)
    nstate_2power = nstate_2power * 2;

  print("power2 nstate");
  print(nstate_2power);

  pd_a3p bool[[1]] ndet_accept(ndet_nstate) = ndet_automata[ndet_nstate*nlabels,:];
  pd_a3p bool[[1]] ndet_start(ndet_nstate) = ndet_automata[ndet_nstate*nlabels+1,:];

  pd_a3p uint64[[1]] classes(ndet_nstate);
  classes = (uint64)ndet_accept;

  pd_a3p uint64[[2]] automataInt(ndet_nstate*(nlabels+2),ndet_nstate);
  automataInt = (uint64)ndet_automata;

  print("classes");
  printVector(declassify(classes));

  uint64 i;
  uint64 j;
  uint64 k;
  uint64 l;

  for (k=0; k<ndet_nstate; k=k+1) {
    classes[k] = classes[k] + 1;
  }

  pd_a3p uint64[[1]] ndet_nstateV(ndet_nstate);
  pd_a3p uint64[[1]] indexesV(nstate_2power);
  ndet_nstateV = (ndet_nstate+1);

  for (k=0; k<nstate_2power; k=k+1) {
    indexesV[k] = k;
  }

  for (k=0; k<ndet_nstate; k=k+1) {
    print("k");
    print(k);
	    
    pd_a3p uint64[[1]] targetClasses(ndet_nstate);
    for (i=0; i<ndet_nstate; i=i+1) {
      pd_a3p uint64[[1]] targets1(ndet_nstate);
      targets1 = automataInt[i*nlabels+l,:];
      targets1 = targets1 * classes;
      targetClasses[i] = sum(targets1);
    }
    // here
    print("computed target class\n");
    printVector(declassify(targetClasses));

    pd_a3p uint64[[1]] class1(ndet_nstate);
    class1 = (classes * ndet_nstateV) + targetClasses;

    if (false) {
      pd_a3p uint64[[1]] class2(ndet_nstate);
      pd_a3p uint64[[1]] new_class(ndet_nstate);
      pd_a3p uint64[[1]] curr_class(ndet_nstate);
      pd_a3p bool[[1]] eq_class(ndet_nstate);
      pd_a3p uint64[[1]] eq_classInt(ndet_nstate);
      pd_a3p uint64[[1]] noteq_classInt(ndet_nstate);
      for (i=0; i<ndet_nstate; i=i+1) {
	class2[i] = i+1;
      }
      for (i=0; i<ndet_nstate; i=i+1) {
	curr_class = class1[i];
	
	eq_class = (curr_class == class1);
	eq_classInt = (uint64)eq_class;
	noteq_classInt = 1;
	noteq_classInt = noteq_classInt - eq_classInt;
	new_class = class2[i];
	class2 = (class2*noteq_classInt) + (new_class * eq_classInt);
      }
      print("computed class 2\n");
      printVector(declassify(class2));
      classes = class2;
    }

    if (true) {
      pd_a3p uint64[[2]] class2(nstate_2power, 3);
      class2[:,0] = indexesV;
      for (i=0; i<ndet_nstate; i=i+1) {
	class2[i,1] = class1[i];
      }

      for (i=ndet_nstate; i<32; i=i+1) {
	class2[i,1] = ndet_nstate+1+(ndet_nstate*ndet_nstate+1);
      }
      pd_a3p uint64[[1]]  sorting1(nstate_2power);

      print("computed class 2\n");
      //printMatrix(declassify(class2));
      class2 = sort(class2, (uint64)1);
      print("class sorted\n");
      //printMatrix(declassify(class2));

      pd_a3p uint64 nclassId = 1;
      class2[0,2] = nclassId;

      pd_a3p uint64 [[2]] class_compare(ndet_nstate-1,2);
      class_compare[:,0] = class2[1:ndet_nstate,1];
      class_compare[:,1] = class2[0:ndet_nstate-1,1];

      pd_a3p bool [[1]] changeClassV(ndet_nstate-1) = ((class_compare[:,0]) != (class_compare[:,1]));
      pd_a3p uint64 [[1]] changeClassInt(ndet_nstate);
      changeClassInt[0] = 1;
      changeClassInt[1:] = (uint64)changeClassV;


      for (i=1; i<ndet_nstate; i=i+1) {
	changeClassInt[i] = changeClassInt[i-1] + changeClassInt[i];
      }
      class2[:,2] = changeClassInt;


      for (i=ndet_nstate; i<nstate_2power; i=i+1) {
	class2[i,2] = ndet_nstate+1;
      }
      print("computed new partition ID\n");
      //printMatrix(declassify(class2));

      // resorting by idx
      class2 = sort(class2, (uint64)0);
      print("resorted class2 by state ID\n");
      printMatrix(declassify(class2));
      for (i=1; i<ndet_nstate; i=i+1) {
	classes[i] = class2[i,2];
      }
      print("computed new partition\n");
      //printVector(declassify(classes));
    }
  }
  print("New partitions\n");
  printVector(declassify(classes));
}


void main() {
  uint64 nstateA = 4;
  uint64 nlabelsA = 2;

  pd_a3p bool[[2]] statesA(nstateA*nlabelsA+2, nstateA);
  pd_a3p bool[[1]] accA(nstateA);
  pd_a3p bool[[1]] startsA(nstateA);

  // (a|b)a
  accA[0] = false;
  startsA[0] = true;
  statesA[0*nlabelsA+0,1] = true;
  statesA[0*nlabelsA+1,1] = true;

  accA[1] = false;
  startsA[1] = false;
  statesA[1*nlabelsA+0,2] = true;
  statesA[1*nlabelsA+1,3] = true;

  accA[2] = true;
  startsA[2] = false;
  statesA[2*nlabelsA+0,3] = true;
  statesA[2*nlabelsA+1,3] = true;

  accA[3] = false;
  startsA[3] = false;
  statesA[3*nlabelsA+0,3] = true;
  statesA[3*nlabelsA+1,3] = true;

  statesA[nstateA*nlabelsA,:] = accA;
  statesA[nstateA*nlabelsA+1,:] = startsA;

  uint64 nstateB = 8;
  uint64 nlabelsB = nlabelsA;

  pd_a3p bool[[2]] statesB(nstateB*nlabelsB+2, nstateB);
  pd_a3p bool[[1]] accB(nstateB);
  pd_a3p bool[[1]] startsB(nstateB);

  // (a|b|ab)a
  accB[0] = true;
  startsB[0] = false;
  statesB[0*nlabelsB+0,1] = true;
  statesB[0*nlabelsB+1,3] = true;

  accB[1] = false;
  startsB[1] = false;
  statesB[1*nlabelsB+0,2] = true;
  statesB[1*nlabelsB+1,4] = true;

  accB[2] = true;
  startsB[2] = false;
  statesB[2*nlabelsB+0,5] = true;
  statesB[2*nlabelsB+1,5] = true;

  accB[3] = false;
  startsB[3] = false;
  statesB[3*nlabelsB+0,2] = true;
  statesB[3*nlabelsB+1,5] = true;

  accB[4] = false;
  startsB[4] = false;
  statesB[4*nlabelsB+0,2] = true;
  statesB[4*nlabelsB+1,5] = true;

  accB[5] = false;
  startsB[5] = false;
  statesB[5*nlabelsB+0,5] = true;
  statesB[5*nlabelsB+1,5] = true;

  statesB[nstateB*nlabelsB,:] = accB;
  statesB[nstateB*nlabelsB+1,:] = startsB;

  //debugAutomaton(statesA);
  //debugAutomaton(statesB);
  print("Initialization done");

  pd_a3p bool[[2]] statesMul = ndet_mult(statesA, statesB);
  print ("mult Done");
  //debugAutomaton(statesMul);

  minimize(statesMul);
  print ("minimiza Done");
  // Publish the results
  publish("nstateA", nstateA + 1);
}
