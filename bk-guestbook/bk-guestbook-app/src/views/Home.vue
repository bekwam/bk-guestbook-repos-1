<template>
  <base-layout class="mt-4">
    <div class="level mb-4">
      <div class="level-left">
        <div class="level-item">
          <h1 class="title">BK Guestbook</h1>
        </div>
        <div class="level-item" v-if="progressActive">
          <progress class="progress" />
        </div>
      </div>
      <div class="level-right">
        <button class="level-item button is-success" @click="showEntryForm">Add A Message</button>
      </div>
    </div>
    <div class="notification" 
        :class="{ 'is-danger': notificationType == 'error', 'is-info': notificationType != 'error' }" 
        v-if="notificationActive">
      <button class="delete" @click="hideNotification"></button>
      {{ notification }}
    </div>
    <div class="card mb-4" v-for="entry in getEntries($route.query.page)" :key="entry.id">    
      <div class="card-content">      
        <div class="media">
          <div class="media-content">
            <h6 class="subtitle is-6">{{ new Date(entry.entryDate).toLocaleDateString() }}</h6>
          </div>
        </div>
        <p class="content">{{ entry.text }}</p>
      </div>
    </div>
    <nav class="pagination" role="navigation" aria-label="pagination">
      
      <button @click="prev"
        :disabled="atStart"
        class="pagination-previous">
        Previous
      </button>
      
      <button @click="next"
        :disabled="atEnd"
        class="pagination-next">
        Next page
      </button>

       <ul class="pagination-list">
        <li v-for="n in $store.state.numPages" :key="n">
          <router-link 
            :to="{ name: 'home', query: { page: n }}" 
            class="pagination-link" :aria-label="`Goto page ${n}`" 
            :class="{ 'is-current': isCurrent(n) }">
            {{ n }}
          </router-link>
        </li>
      </ul>

    </nav>
    <hr />
    <footer class="footer">
      <p class="content">
        Brought to you by Bekwam, Inc &bull; <a href="https://www.bekwam.com">https://www.bekwam.com</a>
      </p>
    </footer>
    <div class="modal" :class="{ 'is-active': entryFormActive }">
      <div class="modal-background"></div>
      <validation-observer tag="div" class="modal-card" v-slot="{ invalid, reset }">
        <header class="modal-card-head">
          <p class="modal-card-title">We'd Love to Hear From You</p>
          <button class="delete" aria-label="close" @click="hideEntryForm(reset)"></button>
        </header>
        <section class="modal-card-body">
          <h2 class="subtitle">Add a Message For Us Below</h2>
          <validation-provider tag="div" class="field" v-slot="{errors, failed}" rules="required|max:1024">
            <div class="control">
              <textarea name="Message Text" class="textarea" v-model="entryText"/>
            </div>
            <p class="help is-danger" v-if="failed">{{ errors[0] }}</p>
          </validation-provider>
        </section>
        <footer class="modal-card-foot">
          <button class="button is-success" @click="saveEntryForm(reset)" :disabled="invalid">Submit</button>
          <button class="button" @click="hideEntryForm(reset)">Cancel</button>
        </footer>
      </validation-observer>
    </div>
  </base-layout>
</template>

<script>
import BaseLayout from "@/layout/BaseLayout.vue";
import { mapState, mapActions, mapGetters } from "vuex";

export default {
  name: 'home',
  components: {
    BaseLayout
  },
  data() {
    return {
      entryFormActive: false,
      notification: null,
      notificationType: null,
      notificationActive: false,
      progressActive: false,
      entryText: null
    }
  },
  computed: {
    ...mapState(["entries"]),
    ...mapGetters(["getEntries"]),
    atStart() { return this.$route.query.page == 1; },
    atEnd() { return this.$route.query.page == this.$store.state.numPages }
  },
  methods: {
    ...mapActions(["fetchEntries","addEntry","fetchPagingInfo"]),
    isCurrent(pageNo) {
      return pageNo == this.$route.query.page;
    },
    prev() {
      let pageNo = this.$route.query.page;
      if( pageNo > 1 ) {
        pageNo--;
      }
      this.$router.push({ name: "home", query: { page: pageNo }});
    },
    next() {
      let pageNo = this.$route.query.page;
      if( pageNo < this.$store.state.numPages ) {
        pageNo++;
      }
      this.$router.push({ name: "home", query: { page: pageNo }});
    },
    showEntryForm() {
      this.entryFormActive = true;
    },
    hideEntryForm(resetValidation) {
      this.entryText = null;
      resetValidation();
      this.entryFormActive = false;      
    },
    async saveEntryForm(resetValidation) {
      this.showProgress();
      try {
        await this.addEntry(this.entryText);
        this.showNotification("Thanks for posting. Your message is being reviewed and will be available shortly.");
      } catch(e) {
        this.showNotification(e, "error");
      }
      resetValidation();
      this.hideProgress();
      this.hideEntryForm(resetValidation);
    },
    showNotification(text, type) {
      this.notification = text;
      if( type ) {
        this.notificationType = type;
      }
      this.notificationActive = true;
    },
    hideNotification() {
      this.notification = null;
      this.notificationType = null;
      this.notificationActive = false;
    },
    showProgress() {
      this.progressActive = true;
    },
    hideProgress() {
      this.progressActive = false;
    },
    async loadPage(vm,toPage) {
      vm.showProgress();
      try {
        await vm.fetchPagingInfo();
        await vm.fetchEntries(toPage);
      } catch(e) {
        vm.showNotification(e, "error");
      }
      vm.hideProgress();
    }
  },
  beforeRouteEnter(to,from,next) {
    if( !to.query.page ) {
      next({ name: "home", query: { page: 1 }});
      return;
    }
    next(vm => vm.loadPage(vm, to.query.page));
  },
  async beforeRouteUpdate(to,from,next) {
    await this.loadPage(this, to.query.page);
    next();
  }
}
</script>
