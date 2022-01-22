module.exports = {
    productionSourceMap: false,
    publicPath: process.env.NODE_ENV === 'production' ? '/portal/' : '/',
      chainWebpack: config => {
        config
            .plugin('html')
            .tap(args => {
                args[0].title = "TicTacToe";
                args[0].bootstrapmincss =  process.env.NODE_ENV === 'production' ? '/portal/bootstrap/bootstrap.min.css':'https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/css/bootstrap.min.css';
                args[0].bootstrapbundleminjs =  process.env.NODE_ENV === 'production' ? '/portal/bootstrap/bootstrap.bundle.min.js':'https://cdn.jsdelivr.net/npm/bootstrap@5.1.0/dist/js/bootstrap.bundle.min.js';
                args[0].bootstrapicons =  process.env.NODE_ENV === 'production' ? '/portal/bootstrap/bootstrap-icons.css':'https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css';
                
                return args;
            })
    }
  }