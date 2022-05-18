import { Link, useLocation } from 'react-router-dom';
import './whiteHeader.scss';
import logo from '@assets/auction-app-logo.PNG';
import SearchBar from '@components/search_bar/searchBar';
import Menu from '@components/menu/menu';
import { useEffect, useState } from 'react';
import { HOME, LOGIN, REGISTER, SHOP, PROFILE, 
  TERMS_CONDITIONS, PRIVACY_POLICY, ABOUT_US,
  FORGOT_PASSWORD, PRODUCT, NEW_PASS } from '@constants';

const WhiteHeader = () => {
  const [hideElements, setHideElements] = useState(false)
  const [activeTab, setActiveTab] = useState("home")
  const location = useLocation()

  useEffect(() => {
    const path = location.pathname
    if([LOGIN, REGISTER, TERMS_CONDITIONS, 
          PRIVACY_POLICY, ABOUT_US, FORGOT_PASSWORD].indexOf(path) > -1  
          || path.includes(NEW_PASS)) 
      setHideElements(true)
    else setHideElements(false)
    if([HOME].indexOf(path) > -1)
      setActiveTab("home")
    else if([SHOP].indexOf(path) > -1 || path.includes(PRODUCT))  
      setActiveTab("shop")
    else if(path.includes(PROFILE))
      setActiveTab("profile")  
    window.scrollTo(0, 0)  
  },[location.pathname]);

  return (
    <header className="white-header">
      <Link to="/">
        <img className={hideElements ? "logo center" : "logo"} src={logo} alt=""/>
      </Link>
      {
        hideElements ? <></> :
      <>
        <SearchBar className="search-bar"/>
        <Menu activeTab={activeTab}/>
      </>
      }
    </header>
  )
}

export default WhiteHeader