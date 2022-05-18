import { Route, Routes } from 'react-router-dom';
import Login from '@pages/login/login';
import Register from '@pages/register/register';
import Home from '@pages/home/home';
import Shop from '@pages/shop/shop';
import Profile from '@pages/profile/profile';
import TermsConditions from '@static/termsConditions';
import PrivacyPolicy from '@static/privacyPolicy';
import AboutUs from '@static/aboutUs';
import OAuth2Login from './oAuth2Login';
import Logout from './logout';
import ForgotPassword from '@pages/forgot_password/forgotPassword';
import Product from '@pages/product/product';
import Seller from '@pages/seller/seller';
import Bids from '@pages/bids/bids';
import Settings from '@pages/settings/settings';
import AddItem from '@pages/add_item/addItem';
import { HOME, LOGIN, REGISTER, SHOP, PROFILE, 
    TERMS_CONDITIONS, PRIVACY_POLICY, ABOUT_US,
    OAUTH, LOGOUT, FORGOT_PASSWORD, PRODUCT, SELLER,
    BIDS, SETTINGS, ADD_ITEM, NEW_PASS, WISHLIST, ADMIN } from '@constants';
import { isAuthenticated, isAdmin } from '@utils/auth';
import NewPassword from '@pages/new_password/newPassword';
import Wishlist from '@pages/wishlist/wishlist';
import Admin from '@pages/admin/admin';

const Links = () => {
    return (
        <Routes>
            <Route path={HOME} element={<Home />} />
            <Route path={LOGIN} element={<Login />} />
            <Route path={REGISTER} element={<Register />} />
            <Route path={SHOP} element={<Shop />} />
            <Route path={PROFILE} element={isAuthenticated() ? <Profile /> : <Home />} />
            <Route path={SELLER} element={isAuthenticated() ? <Seller /> : <Home />} />
            <Route path={BIDS} element={isAuthenticated() ? <Bids /> : <Home />} />
            <Route path={SETTINGS} element={isAuthenticated() ? <Settings /> : <Home />} />
            <Route path={ADD_ITEM} element={isAuthenticated() ? <AddItem /> : <Home />} />
            <Route path={TERMS_CONDITIONS} element={<TermsConditions />} />
            <Route path={PRIVACY_POLICY} element={<PrivacyPolicy />} />
            <Route path={ABOUT_US} element={<AboutUs />} />
            <Route path={OAUTH} element={isAuthenticated() ? <Home /> : <OAuth2Login />} />
            <Route path={LOGOUT} element={<Logout />} />
            <Route path={FORGOT_PASSWORD} element={isAuthenticated() ? <Home /> : <ForgotPassword />} />
            <Route path={PRODUCT + "/:id"} element={<Product />} /> 
            <Route path={NEW_PASS + "/:token"} element={isAuthenticated() ? <Home /> : <NewPassword />} /> 
            <Route path={WISHLIST} element={isAuthenticated() ? <Wishlist /> : <Home />} /> 
            <Route path={ADMIN} element={isAuthenticated() && isAdmin() ? <Admin /> : <Home />} /> 
        </Routes>
    );
}

export default Links;
