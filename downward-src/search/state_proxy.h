#ifndef STATE_PROXY_H
#define STATE_PROXY_H

#include "state.h"
#include "globals.h"

#include <ext/hash_map>


class StateProxy {
    // This class is basically a pointer that can be reassigned even
    // though it is const. This is needed for the hash table below,
    // where a node may first be inserted with a pointer to a
    // temporary object as the key, which will then later be replaced
    // with a pointer to a heap-allocated object generated by
    // make_permanent below.
    // Despite the const-ness, mutating the key of a hash_map in place
    // is fine as long as the mutation does not affect the hash value
    // (which this one does not, as it only changes the address of the
    // state, whereas the contents of the state are used for hashing).

    // Update: This was changed from a State* to an state_var_t*
    //         that points "inside" the state to shave off some memory.
public:
    mutable state_var_t *state_data;
    mutable short *state_data_plans; // values for vars-plans

    StateProxy() {
        state_data = 0;
        state_data_plans = 0;
    }

    StateProxy(state_var_t *state_data_, short *state_data_plans_) {
        state_data = state_data_;
        state_data_plans = state_data_plans_;
    }

    explicit StateProxy(const State *state) {
        state_data = const_cast<state_var_t *>(state->get_buffer());
        state_data_plans = const_cast<short *>(state->get_buffer_plans());
    }

    const StateProxy &operator=(const StateProxy &other) const {
        state_data = other.state_data;
        state_data_plans = other.state_data_plans;
        return *this;
    }
    bool operator==(const StateProxy &other) const {
        return State(state_data, state_data_plans) == State(other.state_data, state_data_plans);
    }
    void make_permanent() const {
        state_var_t *new_buffer = new state_var_t[g_variable_domain.size()];
        for (int i = 0; i < g_variable_domain.size(); i++)
            new_buffer[i] = state_data[i];
        state_data = new_buffer;

        short *new_buffer_plans = new short[g_prev_plans_size];
        for (int i = 0; i < g_prev_plans_size; i++)
            new_buffer_plans[i] = state_data_plans[i];
        state_data_plans = new_buffer_plans;
    }
};

namespace __gnu_cxx {
template<>
struct hash<StateProxy> {
    size_t operator()(const StateProxy &state_proxy) const {
        return State(state_proxy.state_data, state_proxy.state_data_plans).hash();
    }
};
}


#endif
