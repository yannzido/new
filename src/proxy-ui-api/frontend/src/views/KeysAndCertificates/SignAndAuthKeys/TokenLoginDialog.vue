<template>
  <simpleDialog
    :dialog="dialog"
    title="login.logIn"
    @save="save"
    @cancel="cancel"
    saveButtonText="login.logIn"
    :disableSave="!isValid"
    :loading="loading"
  >
    <div slot="content">
      <div class="dlg-edit-row">
        <div class="dlg-row-title">{{$t('fields.tokenPin')}}</div>
        <ValidationProvider
          rules="required"
          ref="tokenPin"
          name="tokenPin"
          v-slot="{ errors }"
          class="validation-provider"
        >
          <v-text-field
            type="password"
            v-model="pin"
            single-line
            class="dlg-row-input"
            name="tokenPin"
            :error-messages="errors"
            v-on:keyup.enter="save"
          ></v-text-field>
        </ValidationProvider>
      </div>
    </div>
  </simpleDialog>
</template>


<script lang="ts">
import Vue from 'vue';
import { ValidationProvider, ValidationObserver } from 'vee-validate';
import SimpleDialog from '@/components/ui/SimpleDialog.vue';
import * as api from '@/util/api';

export default Vue.extend({
  components: { SimpleDialog, ValidationProvider, ValidationObserver },
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
    tokenId: {
      type: String,
    },
  },

  computed: {
    isValid(): boolean {
      // Check that input is not empty
      if (this.pin && this.pin.length > 0) {
        return true;
      }
      return false;
    },
  },

  data() {
    return {
      pin: '',
      loading: false,
    };
  },

  methods: {
    cancel(): void {
      this.$emit('cancel');
      this.clear();
    },
    save(): void {
      this.loading = true;
      api
        .put(`/tokens/${this.tokenId}/login`, {
          password: this.pin,
        })
        .then((res) => {
          this.loading = false;
          this.$emit('save');
        })
        .catch((error) => {
          this.loading = false;
          if (
            error.response.status === 400 &&
            error.response.data.error.code === 'pin_incorrect'
          ) {
            (this.$refs.tokenPin as InstanceType<
              typeof ValidationProvider
            >).setErrors([this.$t('keys.incorrectPin') as string]);
          }

          this.$bus.$emit('show-error', error.message);
        });

      this.clear();
    },
    clear(): void {
      this.pin = '';
      (this.$refs.tokenPin as InstanceType<typeof ValidationProvider>).reset();
    },
  },
});
</script>

<style lang="scss" scoped>
@import '../../../assets/dialogs';
</style>

