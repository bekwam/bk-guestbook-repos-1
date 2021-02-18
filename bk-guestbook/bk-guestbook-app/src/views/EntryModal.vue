<template>
    <div class="modal" :class="{ 'is-active': isActive }">
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
</template>    
<script>
import { mapActions } from "vuex";

export default {
    props: {
        isActive: {
            required: true,
            type: Boolean
        }
    },
    data() {
        return {
            entryText: null
        };
    },
    methods: {
        ...mapActions(["addEntry"]),
        hideEntryForm(resetValidation) {
            resetValidation();
            this.entryText = null;
            this.$emit("update-is-active", false);
        },
        async saveEntryForm(resetValidation) {
            this.$emit("update-entry-text", this.entryText);
            this.hideEntryForm(resetValidation);
        },
    }
};
</script>