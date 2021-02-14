import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    entries: [],
    loading: false,
    lastStatus: null
  },
  mutations: {
    SET_LOADING: (state,loading) => state.loading = loading,
    SET_ENTRIES: (state,entries) => state.entries = entries,
    SET_LAST_STATUS: (state,lastStatus) => state.lastStatus = lastStatus
  },
  actions: {
    async fetchEntries({commit}) {
      let response = await fetch(process.env.VUE_APP_API_URL + "/entries");
      let data = await response.json();
      commit("SET_ENTRIES", data);
    },
    async addEntry({commit}, entryText) {
      commit("SET_LAST_STATUS", null);
      const response = await fetch(process.env.VUE_APP_API_URL + "/entries", {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ "text": entryText })
      });
      commit("SET_LAST_STATUS", response.lastStatus);
    }
  }
})
