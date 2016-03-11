#include "state.h"

#include "axioms.h"
#include "globals.h"
#include "operator.h"
#include "utilities.h"

#include <algorithm>
#include <iostream>
#include <cassert>
using namespace std;

void State::_allocate() {
    borrowed_buffer = false;
    _isFail = false;
    vars = new state_var_t[g_variable_domain.size()];
    vars_plans = new short[g_prev_plans_size];
}

void State::_deallocate() {
    if (!borrowed_buffer) {
        delete[] vars;
        delete[] vars_plans;
    }
}

void State::_copy_buffer_from_state(const State &state) {
    // TODO: Profile if memcpy could speed this up significantly,
    //       e.g. if we do blind A* search.
    for (int i = 0; i < g_variable_domain.size(); i++)
        vars[i] = state.vars[i];
    for (int i = 0; i < g_prev_plans_size; i++)
        vars_plans[i] = state.vars_plans[i];
    _isFail = state._isFail;
}

State & State::operator=(const State &other) {
    if (this != &other) {
        if (borrowed_buffer) {
            _allocate();
        }
        _copy_buffer_from_state(other);
    }
    return *this;
}

State::State(istream &in) {
    _allocate();
    check_magic(in, "begin_state");
    for (int i = 0; i < g_variable_domain.size(); i++) {
        int var;
        in >> var;
        vars[i] = var;
    }
    check_magic(in, "end_state");

    for (int i = 0; i < g_prev_plans_size; i++) {
        vars_plans[i] = 0;
    }

    g_default_axiom_values.assign(vars, vars + g_variable_domain.size());
}

State::State(const State &state) {
    _allocate();
    _copy_buffer_from_state(state);
}

State::State(const State &predecessor, const Operator &op) {
    _init(predecessor, op, false);
}

State::State(const State &predecessor, const Operator &op, bool varsOnly) {
    _init(predecessor, op, varsOnly);
}
void State::_init(const State &predecessor, const Operator &op, bool varsOnly) {
    assert(!op.is_axiom());
    _allocate();
    _copy_buffer_from_state(predecessor);
    // Update values affected by operator.
    for (int i = 0; i < op.get_pre_post().size(); i++) {
        const PrePost &pre_post = op.get_pre_post()[i];
        if (pre_post.does_fire(predecessor))
            vars[pre_post.var] = pre_post.post;
    }

    _isFail = false;

    if (!varsOnly) {

        for (int i = 0; i < g_prev_plans_size; i++) {

            short vars_plans_index = predecessor.vars_plans[i];

            if (vars_plans_index >= 0 && vars_plans_index < g_prev_plans[i].size() && g_prev_plans[i][vars_plans_index].find("*") == 0) {
                _isFail = true;
            } else {

                if ( (predecessor.vars_plans[i] >= 0)
                    && (
                         (op.get_name().find("int_") == 0)
                        || _planContains(g_prev_plans[i], op.get_name(), vars_plans_index)
                        || _isRepeatingExternalAction(op.get_name(), g_prev_plans[i], predecessor.vars_plans[i])
                        )
                   ) {
                    if (vars_plans_index < g_prev_plans[i].size() && g_prev_plans[i][vars_plans_index].find("*") == 0) {
                        _isFail = true;
                    }
                    vars_plans[i] = vars_plans_index;
                } else {
                    vars_plans[i] = -1;
                }
            }
        }
    } else {
        for (int i = 0; i < g_prev_plans_size; i++) {
            vars_plans[i] = 0;
        }
    }

    g_axiom_evaluator->evaluate(*this);
}

bool State::_isRepeatingExternalAction(const string& op, const vector< string >& plan, short index) {
    --index;
    if ( op.find("ext_") != 0 ) {
        return false;
    } if (plan.size() <= index ) {
        return false;
    }else {
        while (index >= 0) {
            if ( plan[index].find("int_") == 0 ) {
                --index;
                continue;
            } else {
                return op == plan[index];
            }
        }
        return false;
    }
}

/**
 * Changes index to point to next possible action in the plan
 */
bool State::_planContains(const vector< string >& plan, const string& op, short& index) {
    for (size_t i = index; i < plan.size(); ++i) {
        if ( plan[i].find("int_") == 0 ) {
            continue;
        } else if ( op == plan[i] ) {
            index = i+1;
            return true;
        } else {
            return false;
        }
    }

    return false;
}

bool State::isDifferent() const {
    for (int i = 0; i < g_prev_plans_size; i++) {
        if (vars_plans[i] >= 0) {
            return false;
        }
    }
    return true;
}

State::~State() {
    _deallocate();
}

void State::dump_pddl() const {
    for (int i = 0; i < g_variable_domain.size(); i++) {
        const string &fact_name = g_fact_names[i][vars[i]];
        if (fact_name != "<none of those>")
            cout << fact_name << endl;
    }
}

void State::dump_fdr() const {
    // We cast the values to int since we'd get bad output otherwise
    // if state_var_t == char.
    for (int i = 0; i < g_variable_domain.size(); i++)
        cout << "  #" << i << " [" << g_variable_name[i] << "] -> "
             << static_cast<int>(vars[i]) << endl;
    cout << "  #";
    for (int i = 0; i < g_prev_plans_size; i++)
        cout << "  " << static_cast<int>(vars_plans[i]);
    cout << endl;
}

bool State::operator==(const State &other) const {
    int size = g_variable_domain.size();
    return ::equal(vars, vars + size, other.vars) && ::equal(vars_plans, vars_plans + g_prev_plans_size, other.vars_plans);
}

bool State::operator<(const State &other) const {
    int size = g_variable_domain.size();
    if ( ::equal(vars, vars + size, other.vars) ) {
        return ::lexicographical_compare(vars_plans, vars_plans + g_prev_plans_size,
                                         other.vars_plans, other.vars_plans + g_prev_plans_size);
    } else {
        return ::lexicographical_compare(vars, vars + size,
                                         other.vars, other.vars + size);
    }
}

size_t State::hash() const {
    return ::hash_number_sequence(vars, g_variable_domain.size()) + 37 * ::hash_number_sequence(vars_plans, g_prev_plans_size);
}

void State::writeTo(ofstream& outStream) const {
    outStream << "begin_state" << endl;
    for (int i = 0; i < g_variable_domain.size(); i++)
        outStream << static_cast<int>(vars[i]) << endl;
    outStream << "end_state" << endl;
}
