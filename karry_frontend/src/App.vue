<template>
  <PageHeader />
  <router-view />
  <PageFooter />
</template>

<script>
import PageHeader from "@/components/PageHeader";
import PageFooter from "@/components/PageFooter";

export default {
  components: {
    PageHeader,
    PageFooter,
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
