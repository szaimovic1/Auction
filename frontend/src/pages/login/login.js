import LoginForm from '@components/login_form/loginForm';
import { Navigate } from 'react-router-dom';
import { isAuthenticated } from '@utils/auth';

const Login = () => {

  return (
    <>
    {!isAuthenticated() ?
      <LoginForm /> :
      <Navigate to="/"/>
    }
    </>
  )
}

export default Login