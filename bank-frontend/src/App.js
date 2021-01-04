import { Redirect, Route, Switch, withRouter} from 'react-router-dom';
import './App.css';
import EnterCreditData from './components/EnterCreditCardData/EnterCreditCardData';
import MerchantAccountList from './components/MerchantAccount/MerchantAccountList/MerchantAccountList';
import CustomToolbar from './Navigation/Toolbar';
import React, { Suspense } from 'react';
import AddMerchantAccount from './components/MerchantAccount/AddMerchantAccount/AddMerchantAccount';

const App = props => {

  let routes = (
    <Switch>
      <Route path="/payment/confirm/:id" render={(props) => <EnterCreditData {...props} />} />
      <Route path="/merchants" render={(props) => <MerchantAccountList {...props} />} />
      <Route path="/add-merchant" render={(props) => <AddMerchantAccount {...props} />} />
      <Redirect to="/" />
    </Switch>
  );

  return (
    <div>
      <CustomToolbar />
      <Suspense fallback={<p>Loading...</p>}>{routes}</Suspense>
      <main>
          {props.children}
        </main>
    </div>
  );
}

export default withRouter(App);
