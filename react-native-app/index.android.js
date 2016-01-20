/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 */
'use strict';

var React = require('react-native');
var {
  AppRegistry
} = React;

var BookListV2 = require('./src/components/app/Bestsellers/BookListV2.js');

AppRegistry.registerComponent('ReactNativeApp', () => BookListV2);
