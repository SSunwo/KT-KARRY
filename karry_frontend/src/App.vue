<script setup>
import { RouterLink, RouterView } from 'vue-router'
</script>

<template>
  <div class="min-h-screen flex flex-col">
    <header>
      <nav class="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8">
        <div class="flex lg:flex-1">
          <a href="#" class="-m-1.5 p-1.5">
            <span class="sr-only">Your Company</span>
            <img class="h-8 w-auto" src="../src/assets/muco.png" alt="" />
          </a>
        </div>
        <div class="hidden lg:flex lg:gap-x-12">
          <RouterLink to="/" class="text-sm/6 font-semibold text-gray-900">Home</RouterLink>
          <RouterLink to="/about" class="text-sm/6 font-semibold text-gray-900">About</RouterLink>
          <RouterLink to="/shippment" class="text-sm/6 font-semibold text-gray-900"
            >Main가자</RouterLink
          >
          <RouterLink to="/signin" class="text-sm/6 font-semibold text-gray-900"
            >Sign in</RouterLink
          >
        </div>
      </nav>
    </header>
    <div class="flex-1 flex flex-col h-full sm:flex-row">
      <main class="flex-1 flex flex-col sm:flex-row bg-gray-300 justify-center items-center">
        <RouterView />
      </main>
    </div>
    <footer class="bg-gray-100 p-2">Footer</footer>
  </div>
</template>

<style scoped></style>
