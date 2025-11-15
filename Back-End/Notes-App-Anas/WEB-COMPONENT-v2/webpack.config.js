import path from 'path';
import HtmlWebpackPlugin from 'html-webpack-plugin';
import CopyWebpackPlugin from 'copy-webpack-plugin';
import MiniCssExtractPlugin from 'mini-css-extract-plugin';

const isProd = process.env.NODE_ENV === 'production';

export default {
  entry: './src/main.js',
  output: {
    path: path.resolve(process.cwd(), 'dist'),
    filename: isProd ? 'assets/js/[name].[contenthash].js' : 'assets/js/[name].js',
    chunkFilename: isProd ? 'assets/js/[name].[contenthash].chunk.js' : 'assets/js/[name].chunk.js',
    assetModuleFilename: 'assets/[hash][ext][query]',
    clean: true
  },
  module: {
    rules: [
      {
        test: /\.m?js$/,
        exclude: /node_modules/,
        use: {
            loader: 'babel-loader'
        }
        },
      {
        test: /\.css$/i,
        use: [
          isProd ? MiniCssExtractPlugin.loader : 'style-loader',
          'css-loader'
        ]
      },
      {
        test: /\.(png|jpe?g|gif|svg|webp|ico)$/i,
        type: 'asset/resource'
      }
    ]
  },
  plugins: [
    new HtmlWebpackPlugin({
      template: './public/index.html',
      minify: isProd && {
        collapseWhitespace: true,
        removeComments: true
      }
    }),
    new CopyWebpackPlugin({
      patterns: [
        { from: 'public/favicon.ico', to: 'favicon.ico' }
      ]
    }),
    ...(isProd ? [new MiniCssExtractPlugin({ filename: 'assets/css/[name].[contenthash].css' })] : [])
  ],
  devtool: isProd ? 'source-map' : 'eval-cheap-module-source-map',
  devServer: {
    static: {
      directory: path.resolve(process.cwd(), 'public')
    },
    port: 5173,
    open: true,
    hot: true,
    client: {
      overlay: true
    },
    historyApiFallback: true
  },
  performance: {
    hints: false
  }
};
