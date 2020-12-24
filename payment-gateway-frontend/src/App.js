import withRoot from "./withRoot";
import "./App.css";
import PaymentHome from "./components/PaymentHome/PaymentHome";
import { Switch } from "react-router-dom";
import { Route } from "react-router-dom";
import AddPaymentMethod from "./components/PaymentMethodsManagement/AddPaymentMethod/AddPaymentMethod";
import AddPaymentMethodSuccess from './components/PaymentMethodsManagement/AddPaymentMethod/AddPaymentMethodSuccess';
import PaymentMethodList from './components/PaymentMethodsManagement/PaymentMethodList/PaymentMethodList';

const App = (props) => {
  return (
    <div className="App">
      <Switch>
        <Route
          path="/payment/:id"
          render={(props) => <PaymentHome {...props} />}
        />
        <Route
          path="/payment-methods"
          render={() => <PaymentMethodList {...props} />} />
        <Route
          path="/add-payment-method"
          render={() => <AddPaymentMethod {...props} />} />
        <Route
          path="/add-payment-method-success"
          render={() => <AddPaymentMethodSuccess {...props} />} />
        <Route
          path="/"
          render={() => <h1> Welcome, navigate to payment/:id. </h1>}
        />

      </Switch>
    </div>
  );
};

export default withRoot(App);
