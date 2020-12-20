import withRoot from "./withRoot";
import "./App.css";
import PaymentHome from "./components/PaymentHome/PaymentHome";
import { Switch } from "react-router-dom";
import { Route } from "react-router-dom";

const App = (props) => {
  return (
    <div className="App">
      <Switch>
        <Route
          path="/payment/:id"
          render={(props) => <PaymentHome {...props} />}
        />
        <Route
          path="/"
          render={() => <h1> Welcome, navigate to payment/:id. </h1>}
        />
      </Switch>
    </div>
  );
};

export default withRoot(App);
