const path = require('path');

module.exports = {
  webpack: {
    alias: {
      "@components": path.resolve(__dirname, 'src/components/'),
      "@assets": path.resolve(__dirname, 'src/assets/'),
      "@layouts": path.resolve(__dirname, 'src/layouts/'),
      "@pages": path.resolve(__dirname, 'src/pages/'),
      "@routes": path.resolve(__dirname, 'src/routes/'),
      "@utils": path.resolve(__dirname, 'src/utils/'),
      "@static": path.resolve(__dirname, 'src/pages/static/'),
      "@constants": path.resolve(__dirname, 'src/constants/constants'),
      "@endpoints": path.resolve(__dirname, 'src/constants/endpoints'),
      "@api": path.resolve(__dirname, 'src/api/'),
      "@utils": path.resolve(__dirname, 'src/utils/'),
    }
  }
};