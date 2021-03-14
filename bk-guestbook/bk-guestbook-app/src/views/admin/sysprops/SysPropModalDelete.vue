<template>
  <div class="modal" :class="{ 'is-active': isActive }">
    <div class="modal-background"></div>
    <div class="modal-card" v-slot="{ invalid, reset }">
      <header class="modal-card-head">
        <p class="modal-card-title">Delete System Property</p>
        <button class="delete" aria-label="close" @click="cancelDelete()"></button>
      </header>
      <section class="modal-card-body">
        <p class="notification is-danger" v-if="errorMessage">
          {{ errorMessage }}
        </p>
        <p class="content" v-if="value">
          Are you sure that you want to delete the system property <strong>{{ value.name }}</strong> ?
          Doing so may break the app.
        </p>
      </section>
      <footer class="modal-card-foot">
        <button class="button is-danger" @click="confirmDelete()">Yes, I Understand</button>
        <button class="button" @click="cancelDelete()">Cancel</button>
        <progress class="progress" v-if="loadingFlag" />
      </footer>
    </div>
  </div>
</template>
<script>
export default {
  props: {
    isActive: {
      required: true,
      type: Boolean
    },
    value: {
      required: true,
      type: Object
    },
    loadingFlag: {
      required: true,
      type: Boolean
    },
    errorMessage: {
      required: true,
      type: String
    }
  },
  methods: {
    confirmDelete() {
      this.$emit("delete-sys-prop", { confirmed: true, id: this.value.id });
    },
    cancelDelete() {
      this.$emit("delete-sys-prop", { confirmed: false, id: this.value.id });
    }
  }
};
</script>