#ifndef STATE_H
#define STATE_H

#include <iostream>
#include <vector>
#include <fstream>

using namespace std;

class Operator;

#include "state_var_t.h"
#include "globals.h"

class State {
    state_var_t *vars; // values for vars
    short *vars_plans; // values for vars-plans
    bool borrowed_buffer;
    bool _isFail;
    void _allocate();
    void _deallocate();
    void _copy_buffer_from_state(const State &state);
    bool _planContains(const vector< string >& plan, const string& op, short& index);
    bool _isRepeatingExternalAction(const string& op, const vector< string >& plan, short index);
    void _init(const State &predecessor, const Operator &op, bool varsOnly);

public:
    explicit State(istream &in);
    State(const State &state);
    State(const State &predecessor, const Operator &op);
    State(const State &predecessor, const Operator &op, bool varsOnly);
    ~State();
    State &operator=(const State &other);
    state_var_t &operator[](int index) {
        return vars[index];
    }
    int operator[](int index) const {
        return vars[index];
    }
    void dump_pddl() const;
    void dump_fdr() const;
    bool operator==(const State &other) const;
    bool operator<(const State &other) const;
    size_t hash() const;

    bool isDifferent() const;

    bool isFail() const {return _isFail;};

    explicit State(state_var_t *buffer, short *buffer_plans) {
        vars = buffer;
        vars_plans = buffer_plans;
        borrowed_buffer = true;
    }
    const state_var_t *get_buffer() const {
        return vars;
    }

    const short *get_buffer_plans() const {
        return vars_plans;
    }

    void writeTo(ofstream& outStream) const;
};

#endif
