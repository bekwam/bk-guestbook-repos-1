module.exports = {
  publicPath:
    process.env.NODE_ENV === "production" ? "/guestbook/" : "/",
  pages: {
    index: {
      // entry for the page
      entry: 'src/main.js',
      // the source template
      template: 'public/index.html',
      // output as dist/index.html
      filename: 'index.html',
      // when using title option,
      // template title tag needs to be <title><%= htmlWebpackPlugin.options.title %></title>
      title: 'Index Page',
      // chunks to include on this page, by default includes
      // extracted common chunks and vendor chunks.
      chunks: ['chunk-vendors', 'chunk-common', 'index']
    },
    login: {
      entry: 'src/loginMain.js',
      template: 'public/login.html',
      filename: 'login.html',
      title: 'Login',
      chunks: ['chunk-vendors', 'chunk-common', 'login']
    },
    loginError: {
      entry: 'src/loginErrorMain.js',
      template: 'public/loginError.html',
      filename: 'loginError.html',
      title: 'Login Error',
      chunks: ['chunk-vendors', 'chunk-common', 'loginError']
    },
    admin: {
      entry: "src/adminMain.js",
      template: "public/admin/index.html",
      filename: "admin/index.html",
      title: "Admin",
      chunks: ["chunk-vendors", "chunk-common", "admin"]
    }
  }    
};
