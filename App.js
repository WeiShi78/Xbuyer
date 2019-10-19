import React from 'react';
import { StyleSheet, Text, View,Button, Image,TouchableOpacity} from 'react-native';
import HistoryButton from './components/historyButton';
import { createAppContainer } from 'react-navigation';
import { createStackNavigator } from 'react-navigation-stack';
// import InboxPage from './inboxPage';


class HomeScreen extends React.Component {
 static navigationOptions = {
    title: 'Home',
    headerStyle: {
      backgroundColor: 'mediumseagreen',
    },
    headerTintColor: '#fff',
    headerTitleStyle: {
      fontWeight: 'bold',
    },
  };

    render() {
  return (
       <View style={{
        flex: 1,
        flexDirection: 'column',
        justifyContent: 'space-between',
      }}>
              <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>Hello World!</Text>
      </View>
      
      
          <View style={{height: 100, backgroundColor: 'white',flexDirection:'row',        justifyContent: 'center', alignItems: 'center',}}>
         <TouchableOpacity onPress={() => this.props.navigation.navigate('Inbox')}>
                  <Image source={require('./images/inbox.png')}  style={{width: 40, height: 40,marginRight:100}} />
          </TouchableOpacity>

           <TouchableOpacity>
                  <Image source={require('./images/camera.png')}  style={{width: 40, height: 40,marginRight: 100}}/>
           </TouchableOpacity>

           <TouchableOpacity onPress={() => this.props.navigation.navigate('History')}>  
                  <Image source={require('./images/history.png')}  style={{width: 40, height: 40}} />
          </TouchableOpacity>
          </View>

      </View>
    
  )

  }
}

class InboxPage extends React.Component {
  static navigationOptions = {
    title: 'Inbox',
    headerStyle: {
      backgroundColor: 'mediumseagreen',
    },
    headerTintColor: '#fff',
    headerTitleStyle: {
      fontWeight: 'bold',
    },
  };
  render() {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>Inbox Page</Text>
      </View>
    );
  }
}

class HistoryPage extends React.Component {
  static navigationOptions = {
    title: 'Inbox',
    headerStyle: {
      backgroundColor: 'mediumseagreen',
    },
    headerTintColor: '#fff',
    headerTitleStyle: {
      fontWeight: 'bold',
    },
  };
  render() {
    return (
      <View style={{ flex: 1, alignItems: 'center', justifyContent: 'center' }}>
        <Text>History Page</Text>
      </View>
    );
  }
}


const AppNavigator = createStackNavigator({
    Home: HomeScreen,
    Inbox: InboxPage,
    History: HistoryPage,
  },
  {
    initialRouteName: 'Home',
  });

export default createAppContainer(AppNavigator);