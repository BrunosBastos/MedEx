import { Navigate } from 'react-router-dom';
import DashboardLayout from 'src/components/DashboardLayout';
import MainLayout from 'src/components/MainLayout';
import Dashboard from 'src/pages/Dashboard';
import OrderList from 'src/pages/OrderList.tsx';
import Login from 'src/pages/Login.tsx';
import NotFound from 'src/pages/NotFound';
import ProductList from 'src/pages/ProductList.tsx';
import Register from 'src/pages/Register.tsx';
import AddProduct from './pages/AddProduct.tsx';
import AddSupplier from './pages/AddSupplier.tsx';
import PharmacyProductDetails from './pages/PharmarcyProductDetails.tsx';
import ShoppingCart from './pages/ShoppingCart.tsx';

const routes = (token, isSuperUser) => [
  {
    path: 'app',
    element: token ? <DashboardLayout /> : <Navigate to="/login" />,
    children: [
      // Admin Only
      { path: 'addProduct', element: isSuperUser ? <AddProduct /> : <Navigate to="/404" /> },
      { path: 'addSupplier', element: isSuperUser ?  <AddSupplier /> : <Navigate to="/404" /> },
      { path: 'product/:id', element: isSuperUser ?  <PharmacyProductDetails /> : <Navigate to="/404" /> },
      // Clients Only
      { path: 'shoppingCart', element: !isSuperUser ?  <ShoppingCart /> : <Navigate to="/404" /> },
      // Anyone
      { path: 'dashboard', element: <Navigate to="/app/orders" />/*<Dashboard />*/ },
      { path: 'orders', element: <OrderList /> },
      { path: 'products', element: <ProductList /> },
      { path: '*', element: <Navigate to="/404" /> }
    ],
  },
  {
    path: '/',
    element: !token ? <MainLayout /> : <Navigate to="/app/dashboard" />,
    children: [
      { path: 'login', element: <Login /> },
      { path: 'register', element: <Register /> },
      { path: '404', element: <NotFound /> },
      { path: '/', element: <Navigate to="/app/dashboard" /> },
      { path: '*', element: <Navigate to="/404" /> }
    ]
  }
];

export default routes;
