import Vue from 'vue'
import Vuex from 'vuex'
import LogRocket from 'logrocket';
import createPlugin from 'logrocket-vuex';

Vue.use(Vuex)

LogRocket.init('ynlrws/bkguestbook');

const logrocketPlugin = createPlugin(LogRocket);

export default new Vuex.Store({
  plugins: [logrocketPlugin],
  state: {
    loading: false,
    lastStatus: null,
    recsPerPage: 5,
    numPages: 1,
    pages: []
  },
  mutations: {
    SET_LOADING: (state,loading) => state.loading = loading,
    SET_LAST_STATUS: (state,lastStatus) => state.lastStatus = lastStatus,
    SET_RECS_PER_PAGE: (state, recsPerPage) => state.recsPerPage = recsPerPage,
    SET_NUM_PAGES: (state, numPages) => state.numPages = numPages,
    SET_PAGE: (state, page) => Vue.set(state.pages, page.pageNo-1, page) // 0-based storage
  },
  actions: {
    async fetchEntries({commit},page) {
      let response = await fetch(process.env.VUE_APP_API_URL + "/entries?page=" + page);
      let data = await response.json();
      commit("SET_PAGE", { pageNo: page, entries: data});
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
    },
    async fetchPagingInfo({commit}) {
      const response = await fetch(process.env.VUE_APP_API_URL + "/entries/pagingInfo");
      let data = await response.json();
      commit("SET_RECS_PER_PAGE", data.recsPerPage);
      commit("SET_NUM_PAGES", data.numPages);
    }
  },
  getters: {
    getEntries: state => pageNo => {
      if( state.pages[pageNo-1] ) {
        return state.pages[pageNo-1].entries; // 0-based retrieval
      }
      return null;
    }
  }
})
