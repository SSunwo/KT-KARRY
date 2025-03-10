<!-- <script setup>
import Navbar from './components/Navbar.vue'
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <header>
      <Navbar />
    </header>
    <div class="flex-1 flex flex-col h-full sm:flex-row">
      <main class="flex-1 flex flex-col sm:flex-row bg-gray-300 justify-center items-center">
        <RouterView />
      </main>
    </div>
    <footer class="bg-gray-100 p-2">Footer</footer>
  </div>
</template>

<style scoped></style> -->

<template>
  <AppLayout />
</template>

<script>
import AppLayout from './layouts/AppLayout.vue'

export default {
  components: {
    AppLayout,
  },
}
</script>
