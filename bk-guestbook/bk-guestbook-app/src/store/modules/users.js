export default {
    namespaced: true,
    state: {},
    mutations: {},
    actions: {
        async logout({commit}) {
            commit("SET_LAST_STATUS", null);
            const response = await fetch(process.env.VUE_APP_API_URL + "/logout", {
                method: "POST"
            });
            commit("SET_LAST_STATUS", response.status);
        }
    }
}