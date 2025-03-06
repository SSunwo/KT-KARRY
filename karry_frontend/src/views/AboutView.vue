<template>
  <div class="about">
    <h1>This is an about page</h1>
  </div>
  <div>
    <sidebar class="order-first sm:w-32 bg-purple-200 p-2">
      <div>11</div>
      <div>22</div>
      <div>33</div>
    </sidebar>
  </div>
</template>

<style>
@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
