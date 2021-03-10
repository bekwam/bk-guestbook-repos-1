<template>
  <div class="modal" :class="{ 'is-active': isActive }">
    <div class="modal-background"></div>
    <validation-observer tag="div" class="modal-card" v-slot="{ invalid, reset }">
      <header class="modal-card-head">
        <p class="modal-card-title">Add System Property</p>
        <button class="delete" aria-label="close" @click="hideSysPropForm(reset)"></button>
      </header>
      <section class="modal-card-body">
        <validation-provider tag="div" class="field" v-slot="{errors, failed}" rules="required|max:128">
          <label class="label">Name</label>
          <div class="control">
            <input type="text" class="input" name="Name" v-model="name"/>
          </div>
          <p class="help is-danger" v-if="failed">{{ errors[0] }}</p>
        </validation-provider>
        <validation-provider tag="div" class="field" v-slot="{errors, failed}" rules="required|max:1024">
          <label class="label">Value</label>
          <div class="control">
            <input type="text" class="input" name="Value" v-model="value"/>
          </div>
          <p class="help is-danger" v-if="failed">{{ errors[0] }}</p>
        </validation-provider>
      </section>
      <footer class="modal-card-foot">
        <button class="button is-success" @click="saveSysPropForm(reset)" :disabled="invalid">Submit</button>
        <button class="button" @click="hideSysPropForm(reset)">Cancel</button>
      </footer>
    </validation-observer>
  </div>
</template>
<script>

export default {
  props: {
    isActive: {
      required: true,
      type: Boolean
    }
  },
  data() {
    return {
      name: null,
      value: null
    };
  },
  methods: {
    hideSysPropForm(resetValidation) {
      resetValidation();
      this.name = null;
      this.value = null;
      this.$emit("update-is-active", false);
    },
    async saveSysPropForm(resetValidation) {
      this.$emit("update-sys-prop", { name: this.name, value: this.value });
      this.hideSysPropForm(resetValidation);
    },
  }
};
</script>