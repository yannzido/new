<template>
  <v-dialog :value="dialog" width="750" scrollable persistent>
    <v-card class="xrd-card">
      <v-card-title>
        <span class="headline">{{
          $t('initialConfiguration.anchor.dialogTitle')
        }}</span>
        <v-spacer />
        <i @click="cancel()" data-test="x-close-button"></i>
      </v-card-title>

      <v-card-text style="height: 500px;" class="elevation-0">
        <p>{{ $t('initialConfiguration.anchor.dialogDetails') }}</p>
        <p>{{ $t('initialConfiguration.anchor.dialogQuestion') }}</p>

        <div class="row-wrap">
          <div class="label">{{ $t('initialConfiguration.anchor.hash') }}</div>
          {{ anchorHash }}
        </div>

        <div class="row-wrap">
          <div class="label">
            {{ $t('initialConfiguration.anchor.generated') }}
          </div>
          {{ anchorGenerated }}
        </div>
      </v-card-text>
      <v-card-actions class="xrd-card-actions">
        <v-spacer></v-spacer>

        <large-button
          class="button-margin"
          outlined
          @click="cancel()"
          data-test="cancel-button"
          >{{ $t('action.cancel') }}</large-button
        >

        <large-button @click="save()" data-test="save-button">{{
          $t('action.confirm')
        }}</large-button>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script lang="ts">
import Vue from 'vue';
import LargeButton from '@/components/ui/LargeButton.vue';

export default Vue.extend({
  components: {
    LargeButton,
  },
  props: {
    dialog: {
      type: Boolean,
      required: true,
    },
  },

  methods: {
    cancel(): void {
      this.$emit('cancel');
    },
    save(): void {
      this.$emit('save');
    },
  },
});
</script>

<style lang="scss" scoped>
@import '../../assets/add-dialogs';
</style>
