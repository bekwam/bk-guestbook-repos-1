export default {
    namespaced: true,
    state: {
        sysprops: []
    },
    mutations: {
        SET_SYSPROPS: (state, sysprops) => state.sysprops = sysprops,
        ADD_SYSPROP: (state, sysprop) => state.sysprops.push( sysprop )
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
        }
    }
};
