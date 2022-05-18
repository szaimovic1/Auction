import { isAuthenticated } from '@utils/auth';
import { Navigate } from 'react-router-dom';
import RegisterForm from '@components/register_form/registerForm';

const Register = () => {
  return (
    <>
    {!isAuthenticated() ?
      <RegisterForm /> :
      <Navigate to="/"/>
    }
    </>
  )
}

export default Register