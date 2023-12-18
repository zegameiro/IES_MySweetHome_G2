import { useState, useEffect } from 'react';
import '../../utils/index.css';
import { Formik, Field, Form } from 'formik';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

import { FaEye, FaEyeSlash } from 'react-icons/fa';

import NavbarSimple from '../layout/NavbarSimple';
import logo from '/src/assets/icon/msh_logo.png';
import { BASE_API_URL } from '../../constants';

const LoginPage = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [loginError, setLoginError] = useState(false);
  const [redirected, setRedirected] = useState(
    new URLSearchParams(window.location.search).get('redirect') !== null
  );

  useEffect(() => {
    if (redirected) {
      console.log('redirected');
    }
  }, []);

  useEffect(() => {
    setTimeout(() => {setRedirected(false)}, 3000);
  }, [redirected]);

  const handleLogin = async (data) => {
    try {
      const res = await axios.get(`${BASE_API_URL}/user/login`, {
        params: {
          email: data.email,
          password: data.password,
        },
      });
      console.log(res);
      if (res.status === 200) {
        localStorage.setItem('user', JSON.stringify(res.data));
        navigate('/dashboard');
      }
    } catch (error) {
      console.log(error);
      setLoginError(true);
    }
  };

  return (
    <div>
      <NavbarSimple fixed />
      <div className="hero min-h-screen bg-gradient-to-br from-base-100 via-secondary via-50% to-primary to-90%">
        <div className="flex-col hero-content lg:flex-row-reverse">
          <div className="flex flex-row items-center p-4 text-center bg-white bg-opacity-75 lg:text-left rounded-xl">
            <img
              src={logo}
              className="h-40"
              alt=""
            />
            <span className="py-6 ml-8">
              <h1 className="text-4xl font-bold text-primary">Welcome Back!</h1>
              <p className="text-xl text-base-content">
                Connect to your home intelligence by signing in. {<br />}
                Access your smart home with ease. Log in to your account.{' '}
                {<br />}
                Don't have an account?{' '}
                <a
                  href="/register"
                  className="font-bold text-primary hover:text-accent"
                >
                  Sign up
                </a>
              </p>
            </span>
          </div>

          <div className=" gradient-blue mx-[5%] w-1/2 rounded-3xl p-2">
            <div className="w-full h-full p-4 bg-base-100 rounded-2xl">
              <Formik
                initialValues={{
                  email: '',
                  password: '',
                }}
                validateOnBlur={true}
                onSubmit={(values, actions) => {
                  console.log(values);
                  handleLogin(values);
                  actions.resetForm();
                }}
              >
                {({ errors, touched }) => (
                  <Form className="flex flex-wrap">
                    {loginError && (
                      <div
                        role="alert"
                        className="alert alert-error"
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/sv"
                          className="w-6 h-6 stroke-current shrink-0"
                          fill="none"
                          viewBox="0 0 24 24"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth="2"
                            d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z"
                          />
                        </svg>
                        <span>Invalid email or password!</span>
                      </div>
                    )}
                    {redirected && (
                      <div
                        role="alert"
                        className="alert alert-warning "
                      >
                        <svg
                          xmlns="http://www.w3.org/2000/svg"
                          className="w-6 h-6 stroke-current shrink-0"
                          fill="none"
                          viewBox="0 0 24 24"
                        >
                          <path
                            strokeLinecap="round"
                            strokeLinejoin="round"
                            strokeWidth="2"
                            d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z"
                          />
                        </svg>
                        <span>
                          You must be logged in to access you dashboard!
                        </span>
                      </div>
                    )}
                    <span className="flex flex-col w-full p-2">
                      <label
                        className="p-2 text-2xl font-medium "
                        htmlFor="email"
                      >
                        Email
                      </label>
                      <Field
                        name="email"
                        placeholder="Email"
                        type="email"
                        className="input input-primary"
                      />
                    </span>
                    <span className="flex flex-col w-full p-2">
                      <label
                        className="p-2 text-2xl font-medium "
                        htmlFor="password"
                      >
                        Password
                      </label>
                      <div className="join">
                        <Field
                          name="password"
                          placeholder="Password"
                          type={showPassword ? 'text' : 'password'}
                          className={` join-item w-full input input-${
                            errors.password && touched.password
                              ? 'error'
                              : 'primary'
                          }`}
                        />
                        <button
                          type="button"
                          className="text-xl text-white btn btn-primary join-item"
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </button>
                      </div>
                    </span>
                    <button
                      type="submit"
                      className="w-full mt-8 text-xl text-white btn btn-primary"
                    >
                      Log in
                    </button>
                  </Form>
                )}
              </Formik>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default LoginPage;
