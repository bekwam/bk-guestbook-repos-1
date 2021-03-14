<template>
  <div class="modal" :class="{ 'is-active': isActive }">
    <div class="modal-background"></div>
    <validation-observer tag="div" class="modal-card" v-slot="{ invalid, reset }">
      <header class="modal-card-head">
        <p class="modal-card-title">{{ title }}</p>
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
            <input type="text" class="input" name="Value" v-model="propValue"/>
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
    },
    title: {
      required: true,
      type: String
    },
    value: {
      required: true,
      type: Object
    }
  },
  data() {
    return {
      id: null,
      name: null,
      propValue: null
    };
  },
  watch: {
    value(val) {
      if( val ) {
        this.id = val.id;
        this.name = val.name;
        this.propValue = val.value;
      }
    }
  },
  methods: {
    resetForm(resetValidation) {
      resetValidation();
      this.id = null;
      this.name = null;
      this.propValue = null;
    },
    hideSysPropForm(resetValidation) {
      this.resetForm(resetValidation);
      this.$emit("save-sys-prop", { confirmed: false, id: this.id, name: this.name, value: this.propValue });
    },
    saveSysPropForm(resetValidation) {
      this.$emit("save-sys-prop", {confirmed: true, id: this.id, name: this.name, value: this.propValue});
      this.resetForm(resetValidation);
    },
  }
};
</script>