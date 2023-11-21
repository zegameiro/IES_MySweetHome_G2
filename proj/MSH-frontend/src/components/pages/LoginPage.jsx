import { useState } from 'react';
import '../../utils/index.css';
import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';

import { FaEye, FaEyeSlash } from 'react-icons/fa';

import NavbarSimple from '../layout/NavbarSimple';
import logo from '/src/assets/msh_logo.png';

const LoginPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  return (
    <div>
      <NavbarSimple fixed />
      <div className="hero min-h-screen bg-gradient-to-br from-base-100 via-secondary via-50% to-primary to-90%">
        <div className="hero-content flex-col lg:flex-row-reverse">
          <div className="text-center flex flex-row lg:text-left bg-white p-4 rounded-xl bg-opacity-75 items-center">
            <img
              src={logo}
              className="h-40"
              alt=""
            />
            <span className="py-6 ml-8">
              <h1 className="text-4xl font-bold text-primary">Welcome Back!</h1>
              <p className="text-xl text-base-content">
                Register to unlock a world of smart living {<br />}
                Join the future of home automation with a MSH account. {<br />}
                Don't have an account?{' '}
                <a
                  href="/register"
                  className="text-primary hover:text-accent font-bold"
                >
                  Sign up
                </a>
              </p>
            </span>
          </div>

          <div className=" gradient-blue mx-[5%] w-1/2 rounded-3xl p-2">
            <div className="bg-base-100 rounded-2xl w-full h-full p-4">
              <Formik
                initialValues={{
                  email: '',
                  password: '',
                }}
                validateOnBlur={true}
                onSubmit={(values) => {
                  console.log(data);
                }}
              >
                {({ errors, touched }) => (
                  <Form className="flex flex-wrap">
                    <span className="flex flex-col w-full p-2">
                      <label
                        className=" font-medium text-2xl p-2"
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
                        className=" font-medium text-2xl p-2"
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
                          className="btn btn-primary join-item text-white text-xl"
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </button>
                      </div>
                    </span>
                    <button
                      type="submit"
                      className="btn btn-primary w-full mt-8 text-xl text-white"
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
