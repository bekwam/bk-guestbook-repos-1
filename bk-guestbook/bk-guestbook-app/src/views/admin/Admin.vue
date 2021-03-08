<template>
  <base-layout class="mt-4">
    <div class="level mb-4">
      <div class="level-left">
        <div class="level-item">
          <h1 class="title">BK Guestbook</h1>
        </div>
        <div class="level-item">
          <progress class="progress" v-if="progressActive"/>
        </div>
      </div>
      <div class="level-right">
        <span class="level-item" href="admin">{{  username }}</span>
        <button class="level-item button is-success">Add A Message</button>
        <button class="level-item button" @click="handleLogout">Logout</button>
      </div>
    </div>
    <p class="notification is-danger" v-if="errorMessage">
      {{ errorMessage }}
    </p>

    <div class="columns">
      <div class="column">
        <aside class="menu">
          <ul class="menu-list">
            <li><router-link :to="{ name: 'entries' }">Home</router-link></li>
            <li><router-link :to="{ name: 'users' }">Users</router-link></li>
            <li><router-link :to="{ name: 'sysprop' }">System Properties</router-link></li>
          </ul>
        </aside>
      </div>
      <div class="column">
        <router-view />
      </div>
    </div>
    <footer class="footer">
      <p class="content">
        Brought to you by Bekwam, Inc &bull; <a href="https://www.bekwam.com">https://www.bekwam.com</a>
      </p>
    </footer>
  </base-layout>
</template>
<script>
import { mapState, mapActions } from "vuex";
import { publicPath } from "../../../vue.config.js";
import BaseLayout from "../../layout/BaseLayout.vue";

export default {
  components: { BaseLayout },
  data() {
    return {
      errorMessage: null,
      progressActive: false
    }
  },
  computed: {
    ...mapState(["lastStatus","username"])
  },
  methods: {
    ...mapActions(["fetchUsername"]),
    ...mapActions("users", ["logout"]),
    async handleLogout() {
      this.errorMessage = null;
      try {
        await this.logout();
        window.location.replace(publicPath);
      } catch(e) {
        this.errorMessage = e;
      }
    }
  },
  beforeRouteEnter(to,from,next) {
    next( vm => {
      try {
        vm.progresActive = true;
        vm.fetchUsername();
      } catch(e) {
        vm.errorMessage = e;
      } finally {
        vm.progressActive = false;
      }
    });
  }
};
</script>