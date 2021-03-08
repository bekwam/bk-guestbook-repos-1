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
        <a class="level-item" href="admin">Admin</a>
        <button class="level-item button is-success" @click="toggleEntryModal(true)">Add A Message</button>
      </div>
    </div>
    <div class="notification" 
        :class="{ 'is-danger': notificationType == 'error', 'is-info': notificationType != 'error' }" 
        v-if="notificationActive">
      <button class="delete" @click="hideNotification"></button>
      {{ notification }}
    </div>
    <div class="message mb-4" v-for="entry in getEntries($route.query.page)" :key="entry.id">    
      <div class="message-body">      
        <h6 class="subtitle is-size-7">{{ new Date(entry.entryDate).toLocaleDateString() }}</h6>
        <p class="content is-size-5">{{ entry.text }}</p>
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
    <footer class="footer">
      <p class="content">
        Brought to you by Bekwam, Inc &bull; <a href="https://www.bekwam.com">https://www.bekwam.com</a>
      </p>
    </footer>
    <entry-modal :isActive="entryFormActive" @update-is-active="toggleEntryModal(false)" @update-entry-text="saveEntryForm" />
  </base-layout>
</template>

<script>
import BaseLayout from "@/layout/BaseLayout.vue";
import { mapState, mapActions, mapGetters } from "vuex";
import EntryModal from "./EntryModal.vue";
import HomeController from "./HomeController";

export default {
  name: 'home',
  components: {
    BaseLayout,
    EntryModal
  },
  data() {
    return {
      entryFormActive: false,
      notification: null,
      notificationType: null,
      notificationActive: false,
      progressActive: false
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
    toggleEntryModal(show) {
      this.entryFormActive = show;
    },
    async saveEntryForm(entryText) {
      this.toggleProgress(true);
      try {
        await this.addEntry(entryText);
        this.showNotification("Thanks for posting. Your message is being reviewed and will be available shortly.");
      } catch(e) {
        this.showNotification(e, "error");
      }
      this.toggleProgress(false);
      this.toggleEntryModal(false);
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
    toggleProgress(show) {
      this.progressActive = show;
    },
    async loadPage(toPage) {
      this.toggleProgress(true);
      try {
        await this.fetchPagingInfo();
        await this.fetchEntries(toPage);
      } catch(e) {
        this.showNotification(e, "error");
      }
      this.toggleProgress(false);
    }
  },
  ...HomeController
}
</script>
