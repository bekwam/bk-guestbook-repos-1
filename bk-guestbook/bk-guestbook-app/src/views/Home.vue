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
    <div class="card mb-4" v-for="entry in entries" :key="entry.id">    
      <div class="card-content">      
        <div class="media">
          <div class="media-content">
            <h6 class="subtitle is-6">{{ new Date(entry.entryDate).toLocaleDateString() }}</h6>
          </div>
        </div>
        <p class="content">{{ entry.text }}</p>
      </div>
    </div>
    <div class="buttons mb-4">
      <button class="button">Prev</button>
      <button class="button">Next</button>
    </div>
    <hr />
    <footer class="footer">
      <p class="content">
        Brought to you by Bekwam, Inc &bull; <a href="https://www.bekwam.com">https://www.bekwam.com</a>
      </p>
    </footer>
    <div class="modal" :class="{ 'is-active': entryFormActive }">
      <div class="modal-background"></div>
      <validation-observer tag="div" class="modal-card" v-slot="{ invalid }">
        <header class="modal-card-head">
          <p class="modal-card-title">We'd Love to Hear From You</p>
          <button class="delete" aria-label="close" @click="hideEntryForm"></button>
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
          <button class="button is-success" @click="saveEntryForm" :disabled="invalid">Submit</button>
          <button class="button" @click="hideEntryForm">Cancel</button>
        </footer>
      </validation-observer>
    </div>
  </base-layout>
</template>

<script>
import BaseLayout from "@/layout/BaseLayout.vue";
import { mapState, mapActions } from "vuex";

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
    ...mapState(["entries"])
  },
  methods: {
    ...mapActions(["fetchEntries","addEntry"]),
    showEntryForm() {
      this.entryFormActive = true;
    },
    hideEntryForm() {
      this.entryText = null;
      this.entryFormActive = false;
    },
    async saveEntryForm() {
      this.showProgress();
      try {
        await this.addEntry(this.entryText);
        this.showNotification("Thanks for posting. Your message is being reviewed and will be available shortly.");
      } catch(e) {
        this.showNotification(e, "error");
      }
      this.hideProgress();
      this.hideEntryForm();
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
    }
  },
  beforeRouteEnter(to,from,next) {
    next(async vm => {
      vm.showProgress();
      try {
        await vm.fetchEntries();
      } catch(e) {
        this.showNotification(e, "error");
      }
      vm.hideProgress();
    });
  }
}
</script>
