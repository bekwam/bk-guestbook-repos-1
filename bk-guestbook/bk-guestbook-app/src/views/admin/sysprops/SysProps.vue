<template>
  <div>
    <div class="level">
      <div class="level-left">
        <div class="level-item buttons">
          <button class="button"
                  @click="setSysPropModal({ show: true, title: 'Add System Property',id: null, name: null, value: null })">
                    Add Property
          </button>
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
              <font-awesome-icon
                  class="mr-4"
                  icon="edit"
                  @click="setSysPropModal({ show: true, title: 'Edit System Property', id: sysprop.id, name: sysprop.name, value: sysprop.value })"
              />
              <font-awesome-icon
                  class="has-text-danger"
                  icon="trash"
                  @click="setSysPropModalDelete({ show: true, id: sysprop.id, name: sysprop.name })"
              />
            </span>
          </td>
        </tr>
      </tbody>
    </table>
    <sys-prop-modal
        :isActive="sysPropFormActive"
        :title="sysPropFormTitle"
        :value="sysPropToEdit"
        @save-sys-prop="saveSysPropForm" />
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
      sysPropFormTitle: "",
      sysPropFormActive: false,
      sysPropModalDelete: false,
      sysPropToDelete: null,
      sysPropToEdit: null,
      modalLoading: false,
      modalErrorMessage: null
    };
  },
  methods: {
    ...mapActions("sysprops", ["fetchSysProps","addSysProp","deleteSysProp","updateSysProp"]),
    setSysPropModal({show, title, name, value, id}) {
      this.sysPropFormActive = show;
      this.sysPropFormTitle = title;
      this.sysPropToEdit = { name, value, id };
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
    async saveSysPropForm({confirmed, id, name, value}) {
      if(confirmed) {
        let operation = "adding";
        try {
          this.modalLoading = true;
          if (id) {
            operation = "editing";
            await this.updateSysProp({id, name, value});
          } else {
            await this.addSysProp({name, value});
          }
        } catch (e) {
          this.modalErrorMessage = `Error ${operation} System Property; ${e}`;
        } finally {
          this.modalLoading = false;
        }
      }
      this.sysPropFormActive = false;
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