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
          <td>
            <span class="icon">
              <font-awesome-icon class="mr-4" icon="edit" />
              <font-awesome-icon
                  class="has-text-danger"
                  icon="trash"
                  @click="setSysPropModalDelete({ id: sysprop.id, name: sysprop.name, show: true })"
              />
            </span>
          </td>
        </tr>
      </tbody>
    </table>
    <sys-prop-modal :isActive="sysPropFormActive" @update-is-active="toggleSysPropModal(false)" @update-sys-prop="saveSysPropForm" />
    <sys-prop-modal-delete
        :isActive="sysPropModalDelete"
        :value="sysPropToDelete"
        @delete-sys-prop="handleDeleteSysProp"/>
  </div>
</template>
<script>
import { mapState, mapActions } from "vuex";
import SysPropModal from "@/views/admin/sysprops/SysPropModal";
import SysPropModalDelete from "@/views/admin/sysprops/SysPropModalDelete";

export default {
  components: {SysPropModal,SysPropModalDelete},
  computed: {
    ...mapState("sysprops", ["sysprops"])
  },
  data() {
    return {
      sysPropFormActive: false,
      sysPropModalDelete: false,
      sysPropToDelete: null,
      modalLoading: false,
      modalErrorMessage: null
    };
  },
  methods: {
    ...mapActions("sysprops", ["fetchSysProps","addSysProp","deleteSysProp"]),
    toggleSysPropModal(val) {
      this.sysPropFormActive = val;
    },
    setSysPropModalDelete({ show, name, id }) {
      this.sysPropModalDelete = show;
      this.sysPropToDelete = { name, id };
    },
    async handleDeleteSysProp(val) {
      if( val.confirmed ) {
        this.modalLoading = true;
        try {
          await this.deleteSysProp(val);
        } catch(e) {
          this.modalErrorMessage = `Error deleting System Property; ${e}`;
        } finally {
          this.modalLoading = false;
        }
      }
      this.setSysPropModalDelete({ name: null, id: null, show: false });
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