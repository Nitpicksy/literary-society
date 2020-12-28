import withRoot from "./withRoot";
import "./App.css";
import PaymentHome from "./components/PaymentHome/PaymentHome";
import { Switch } from "react-router-dom";
import { Route } from "react-router-dom";
import AddPaymentMethod from "./components/PaymentMethodsManagement/AddPaymentMethod/AddPaymentMethod";
import AddCompany from "./components/CompanyManagement/AddCompany/AddCompany";

const App = (props) => {
  return (
    <div className="App">
      <Switch>
        <Route
          path="/payment/:id"
          render={(props) => <PaymentHome {...props} />}
        />
        <Route
          path="/add-payment-method"
          render={() => <AddPaymentMethod {...props} />} />
        <Route
          path="/add-company"
          render={() => <AddCompany {...props} />} />
        <Route
          path="/"
          render={() => <h1> Welcome, navigate to payment/:id. </h1>}
        />

      </Switch>
    </div>
  );
};

export default withRoot(App);
