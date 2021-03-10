<template>
  <div>
    <div class="level">
      <div class="level-left">
        <div class="level-item buttons">
          <button class="button" @click="toggleSysPropModal(true)">Add Property</button>
        </div>
      </div>
    </div>
    <table class="table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Value</th>
          <th>&nbsp;</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="sysprop in sysprops" :key="sysprop.id">
          <td>{{ sysprop.name }}</td>
          <td>{{ sysprop.value }}</td>
          <td>[COMMANDS]</td>
        </tr>
      </tbody>
    </table>
    <sys-prop-modal :isActive="sysPropFormActive" @update-is-active="toggleSysPropModal(false)" @update-sys-prop="saveSysPropForm" />
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";
import SysPropModal from "@/views/admin/sysprops/SysPropModal";

export default {
  components: {SysPropModal},
  computed: {
    ...mapState("sysprops", ["sysprops"])
  },
  data() {
    return {
      sysPropFormActive: false
    };
  },
  methods: {
    ...mapActions("sysprops", ["fetchSysProps","addSysProp"]),
    toggleSysPropModal(val) {
      this.sysPropFormActive = val;
    },
    async saveSysPropForm(prop) {
      await this.addSysProp(prop);
    }
  },
  beforeRouteEnter(to,from,next) {
    next(vm => {
      vm.$store.commit("SET_LOADING", true);
      try {
        vm.fetchSysProps();
      } finally {
        vm.$store.commit("SET_LOADING", false);
      }
    });
  }
};

</script>