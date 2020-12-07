import './App.css';
import SignIn from './components/Authentication/SignIn/SignIn';
import SignUp from './components/Authentication/SignUp/SignUp';
import Logout from './components/Authentication/SignIn/Logout';
import React, { Suspense } from 'react';
import { Route, Switch, withRouter, Redirect } from 'react-router-dom';
import CustomToolbar from './components/Navigation/Toolbar/Toolbar';

//ovo koristis za sve stranice osim za pocetnu!!!Pocetnu stranicu ce user uvek posetiti i zato to ne radis za pocetnu stranicu 
// const Auth = React.lazy(() => {
//   return import('./containers/Auth/Auth');
// });

const App = props => {
  let routes = (
    <Switch>
      <Route path="/sign-in" render={(props) => <SignIn {...props} />} />
      <Route path="/sign-up" render={(props) => <SignUp {...props} />} />
      <Route path="/logout" render={(props) => <Logout {...props} />} />
      <Route path="/" exact component={SignIn} />
      <Redirect to="/" />
    </Switch>
  );

  // if (props.isAuthenticated) {
  //   routes = (
  //     <Switch>
  //       <Route path="/checkout" render={(props) => <Checkout {...props} />} />
  //       <Redirect to="/" />
  //     </Switch>
  //   );
  // }

  return (
    <div>
        {/* <CustomToolbar/> */}
        <Suspense fallback={<p>Loading...</p>}>{routes}</Suspense>
        <main>
          {props.children}
        </main>
    </div>
  );

}


export default withRouter(App);