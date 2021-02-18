export default {
  beforeRouteEnter(to,from,next) {
    if( !to.query.page ) {
      next({ name: "home", query: { page: 1 }});
      return;
    }
    next(vm => vm.loadPage(to.query.page));
  },
  async beforeRouteUpdate(to,from,next) {
    await this.loadPage(to.query.page);
    next();
  }
};
