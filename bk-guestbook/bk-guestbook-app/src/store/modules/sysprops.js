export default {
    namespaced: true,
    state: {
        sysprops: []
    },
    mutations: {
        SET_SYSPROPS: (state, sysprops) => state.sysprops = sysprops,
        ADD_SYSPROP: (state, sysprop) => state.sysprops.push( sysprop ),
        DELETE_SYSPROP: (state, id) => {
            let idx = state.sysprops.findIndex( sp => sp.id == id );
            if( idx != -1 ) {
                state.sysprops.splice(idx, 1);
            }
        },
        UPDATE_SYSPROP: (state, sysprop) => {
            let sp = state.sysprops.find( sp => sp.id == sysprop.id );
            if( sp ) {
                sp.name = sysprop.name;
                sp.value = sysprop.value;
            }
        }
    },
    actions: {
        async fetchSysProps({commit}) {
            let response = await fetch(process.env.VUE_APP_API_URL + "/config");
            let data = await response.json();
            commit("SET_SYSPROPS", data);
        },
        async addSysProp({commit}, prop) {
            let response = await fetch( process.env.VUE_APP_API_URL + "/config", {
                method: "POST",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(prop)
            });
            let id = await response.text();
            prop.id = id;
            commit("ADD_SYSPROP", prop);
        },
        async deleteSysProp({commit}, {id}) {
            await fetch(
                process.env.VUE_APP_API_URL + "/config/" + id,
                { method: "DELETE" }
                );
            commit("DELETE_SYSPROP", id);
        },
        async updateSysProp({commit}, prop) {
            await fetch( process.env.VUE_APP_API_URL + "/config/" + prop.id, {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(prop)
            });
            commit("UPDATE_SYSPROP", prop);
        }
    }
};
