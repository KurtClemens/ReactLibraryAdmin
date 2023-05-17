// pages
import Dashboard from "./views/dashboard/pages/Dashboard";
import Books from "./views/books/pages/book/Books";

// other
import { FC } from "react";
import Loans from "./views/Loans/pages/Loans";
import Requests from "./views/Requests/pages/Requests";
import Users from "./views/Users/pages/Users";
import BookForm from "./views/books/components/bookForm/BookForm";
import CreateReadUpdateBook from "./views/books/pages/create-read-update-book/CreateReadUpdateBook";
import CreateReadUpdateUser from "./views/Users/pages/create-read-update-user/CreateReadUpdateUser";
import Office from "./views/offices/pages/Office";
import CreateReadUpdateOffice from "./views/offices/pages/create-read-update-office/CreateReadUpdateOffice";
import ReadRequest from "./views/Requests/pages/read-request/ReadRequest";
import ReadUpdateLoan from "./views/Loans/pages/read-update-loan/ReadUpdateLoan";
import Login from "./views/login/pages/Login";

// interface
interface Route {
  key: string;
  title: string;
  path: string;
  enabled: boolean;
  component: FC<{}>;
}

export const routes: Array<Route> = [
  {
    key: "default-route",
    title: "Home",
    path: "/",
    enabled: true,
    component: Dashboard,
  },
  {
    key: "home-route",
    title: "Home",
    path: "/home",
    enabled: true,
    component: Dashboard,
  },
  {
    key: "books-route",
    title: "Books",
    path: "/books",
    enabled: true,
    component: Books,
  },
  {
    key: "add-book-route",
    title: "Add Book",
    path: "/books/add",
    enabled: true,
    component: CreateReadUpdateBook,
  },
  {
    key: "read-book-route",
    title: "Read Book",
    path: "/books/:id",
    enabled: true,
    component: CreateReadUpdateBook,
  },
  {
    key: "edit-book-route",
    title: "Edit Book",
    path: "/books/edit/:id",
    enabled: true,
    component: CreateReadUpdateBook,
  },
  {
    key: "loans-route",
    title: "Loans",
    path: "/loans",
    enabled: true,
    component: Loans,
  },
  {
    key: "read-loan-route",
    title: "Read Loan",
    path: "/loans/:id",
    enabled: true,
    component: ReadUpdateLoan,
  },
  {
    key: "edit-loan-route",
    title: "Edit Loan",
    path: "/loans/edit/:id",
    enabled: true,
    component: ReadUpdateLoan,
  },
  {
    key: "requests-route",
    title: "Requests",
    path: "/requests",
    enabled: true,
    component: Requests,
  },
  {
    key: "users-route",
    title: "Users",
    path: "/users",
    enabled: true,
    component: Users,
  },
  {
    key: "add-user-route",
    title: "Add Users",
    path: "/users/add",
    enabled: true,
    component: CreateReadUpdateUser,
  },
  {
    key: "read-user-route",
    title: "Read User",
    path: "/users/:id",
    enabled: true,
    component: CreateReadUpdateUser,
  },
  {
    key: "edit-user-route",
    title: "Edit User",
    path: "/users/edit/:id",
    enabled: true,
    component: CreateReadUpdateUser,
  },
  {
    key: "users-route",
    title: "Offices",
    path: "/offices",
    enabled: true,
    component: Office,
  },
  {
    key: "add-office-route",
    title: "Add Office",
    path: "/offices/add",
    enabled: true,
    component: CreateReadUpdateOffice,
  },
  {
    key: "read-office-route",
    title: "Read Office",
    path: "/offices/:id",
    enabled: true,
    component: CreateReadUpdateOffice,
  },
  {
    key: "edit-office-route",
    title: "Edit Office",
    path: "/offices/edit/:id",
    enabled: true,
    component: CreateReadUpdateOffice,
  },
  {
    key: "requests-route",
    title: "Requests",
    path: "/requests",
    enabled: true,
    component: Requests,
  },
  {
    key: "read-request-route",
    title: "Read Office",
    path: "/requests/:id",
    enabled: true,
    component: ReadRequest,
  },
  {
    key: "login-route",
    title: "Login",
    path: "/login",
    enabled: true,
    component: Login,
  },
];
