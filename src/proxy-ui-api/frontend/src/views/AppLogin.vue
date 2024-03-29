<template>
  <v-app class="app-custom">
    <v-content>
      <v-toolbar dark color="#202020" class="elevation-2">
        <v-img
          :src="require('../assets/xroad_logo_64.png')"
          height="64"
          width="128"
          max-height="64"
          max-width="128"
        ></v-img>
        <v-spacer></v-spacer>
      </v-toolbar>
      <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex sm8 md4 class="set-width">
            <v-card flat>
              <v-toolbar flat class="login-form-toolbar">
                <v-toolbar-title class="login-form-toolbar-title">{{$t('login.logIn')}}</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <ValidationObserver ref="form" v-slot="{ validate }">
                    <ValidationProvider name="username" rules="required" v-slot="{ errors }">
                      <v-text-field
                        id="username"
                        name="username"
                        :label="$t('fields.username')"
                        :error-messages="errors"
                        type="text"
                        v-model="username"
                        @keyup.enter="submit"
                      ></v-text-field>
                    </ValidationProvider>

                    <ValidationProvider name="password" rules="required" v-slot="{ errors }">
                      <v-text-field
                        id="password"
                        name="password"
                        :label="$t('fields.password')"
                        :error-messages="errors"
                        type="password"
                        v-model="password"
                        @keyup.enter="submit"
                      ></v-text-field>
                    </ValidationProvider>
                  </ValidationObserver>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn
                  id="submit-button"
                  color="primary"
                  class="rounded-button"
                  @click="submit"
                  min-width="120"
                  rounded
                  :disabled="isDisabled"
                  :loading="loading"
                >{{$t('login.logIn')}}</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>
    </v-content>
  </v-app>
</template>

<script lang="ts">
import Vue, { VueConstructor } from 'vue';
import { RouteName } from '@/global';
import { ValidationProvider, ValidationObserver } from 'vee-validate';

export default (Vue as VueConstructor<
  Vue & {
    $refs: {
      form: InstanceType<typeof ValidationObserver>;
    };
  }
>).extend({
  name: 'login',
  components: {
    ValidationProvider,
    ValidationObserver,
  },
  data() {
    return {
      loading: false,
      username: 'xrd',
      password: 'secret',
    };
  },
  computed: {
    isDisabled() {
      if (
        this.username.length < 1 ||
        this.password.length < 1 ||
        this.loading
      ) {
        return true;
      }
      return false;
    },
  },
  methods: {
    async submit() {
      // Validate inputs

      const isValid = await this.$refs.form.validate();

      if (!isValid) {
        return;
      }

      const loginData = {
        username: this.username,
        password: this.password,
      };

      this.$refs.form.reset();
      this.loading = true;

      this.$store
        .dispatch('login', loginData)
        .then(
          (response) => {
            // Auth ok. Start phase 2 (fetch user data).
            this.fetchUserData();
          },
          (error) => {
            // Display invalid username/password error in inputs
            if (error.response && error.response.status === 401) {
              // Clear inputs
              this.username = '';
              this.password = '';
              this.$refs.form.reset();

              // The whole view needs to be rendered so the "required" rule doesn't block
              // "wrong unsername or password" error in inputs
              this.$nextTick(() => {
                // Set inputs to error state
                this.$refs.form.setErrors({
                  username: [''],
                  password: [this.$t('login.errorMsg401') as string],
                });
              });
            }
            console.error(error);
            this.$bus.$emit('show-error', error.message);
          },
        )
        .finally(() => {
          // Clear loading state
          this.loading = false;
        });
    },
    async fetchUserData() {
      this.loading = true;
      this.$store
        .dispatch('fetchUserData')
        .then(
          (response) => {
            this.$router.replace({ name: RouteName.Clients });
          },
          (error) => {
            // Display error
            console.error(error);
            this.$bus.$emit('show-error', error.message);
          },
        )
        .finally(() => {
          // Clear loading state
          this.loading = false;
        });
    },
  },
});
</script>

<style lang="scss" scoped>
.app-custom {
  background-color: white;
}

.login-form-toolbar {
  background-color: white;
  border-bottom: 1px #9b9b9b solid;
  margin-bottom: 30px;
  padding-left: 0;
}

.login-form-toolbar-title {
  margin-left: 0;
  color: #4a4a4a;
  font-size: 36px;
  font-weight: 300;
  line-height: 44px;
  margin-left: -24px;
}

.set-width {
  max-width: 420px;
}
</style>

