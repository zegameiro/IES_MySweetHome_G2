import { useState } from 'react';
import '../../utils/index.css';
import { Formik, Field, Form } from 'formik';
import * as Yup from 'yup';

import { FaEye, FaEyeSlash } from 'react-icons/fa';

import NavbarSimple from '../layout/NavbarSimple';
import logo from '/src/assets/msh_logo.png';

const SignupSchema = Yup.object().shape({
  firstName: Yup.string()
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required'),
  lastName: Yup.string()
    .min(2, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required'),
  email: Yup.string().email('Invalid email').required('Required'),
  password: Yup.string()
    .min(8, 'Too Short!')
    .max(50, 'Too Long!')
    .required('Required'),
  confirmPassword: Yup.string()
    .oneOf([Yup.ref('password'), null], 'Passwords must match')
    .required('Required'),
});

const RegisterPage = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);

  return (
    <div>
      <NavbarSimple  fixed />
      <div className="hero min-h-screen bg-gradient-to-br from-base-100 via-secondary via-50% to-primary to-90%">
        <div className="hero-content flex-col lg:flex-row-reverse">
          <div className="text-center flex flex-row lg:text-left bg-white p-4 rounded-xl bg-opacity-75 items-center">
            <img
              src={logo}
              className="h-40"
              alt=""
            />
            <span className="py-6 ml-8">
              <h1 className="text-4xl font-bold text-primary">
                Welcome to MSH
              </h1>
              <p className="text-xl text-base-content">
                Register to unlock a world of smart living {<br />}
                Join the future of home automation with a MSH account. {<br />}
                Already have an account?{' '}
                <a
                  href="/login"
                  className="text-primary hover:text-accent font-bold"
                >
                  Sign in
                </a>
              </p>
            </span>
          </div>

          <div className=" gradient-blue mx-[5%] w-1/2 rounded-3xl p-2">
            <div className="bg-base-100 rounded-2xl w-full h-full p-4">
              <Formik
                initialValues={{
                  firstName: '',
                  lastName: '',
                  email: '',
                  password: '',
                  confirmPassword: '',
                }}
                validationSchema={SignupSchema}
                validateOnBlur={true}
                onSubmit={(values) => {
                  const { confirmPassword, ...data } = values;
                  console.log(data);
                }}
              >
                {({ errors, touched }) => (
                  <Form className="flex flex-wrap">
                    <span className="flex flex-col w-full p-2">
                      <label
                        className=" font-medium text-2xl p-2"
                        htmlFor="firstName"
                      >
                        First Name
                      </label>
                      <Field
                        name="firstName"
                        placeholder="First Name"
                        className={`input input-${
                          errors.firstName && touched.firstName
                            ? 'error'
                            : 'primary'
                        }`}
                      />
                      {errors.firstName && touched.firstName && (
                        <p className="text-error px-2 text-sm">
                          {errors.firstName}
                        </p>
                      )}
                    </span>
                    <span className="flex flex-col w-full p-2">
                      <label
                        className=" font-medium text-2xl p-2"
                        htmlFor="lastName"
                      >
                        Last Name
                      </label>
                      <Field
                        name="lastName"
                        placeholder="Last Name"
                        className={`input input-${
                          errors.lastName && touched.lastName
                            ? 'error'
                            : 'primary'
                        }`}
                      />
                      {errors.lastName && touched.lastName && (
                        <p className="text-error px-2 text-sm">
                          {errors.lastName}
                        </p>
                      )}
                    </span>
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
                        className={`input input-${
                          errors.email && touched.email ? 'error' : 'primary'
                        }`}
                      />
                      {errors.email && touched.email && (
                        <p className="text-error px-2 text-sm">
                          {errors.email}
                        </p>
                      )}
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
                          className={`btn btn-${
                            errors.password && touched.password
                              ? 'error'
                              : 'primary'
                          } join-item text-white text-xl`}
                          onClick={() => setShowPassword(!showPassword)}
                        >
                          {showPassword ? <FaEyeSlash /> : <FaEye />}
                        </button>
                      </div>
                      {errors.password && touched.password && (
                        <p className="text-error px-2 text-sm">
                          {errors.password}
                        </p>
                      )}
                    </span>
                    <span className="flex flex-col w-full p-2">
                      <label
                        className=" font-medium text-2xl p-2"
                        htmlFor="confirmPassword"
                      >
                        Confirm Password
                      </label>
                      <span className="join">
                        <Field
                          name="confirmPassword"
                          placeholder="Confirm Password"
                          type={showConfirmPassword ? 'text' : 'password'}
                          className={` join-item w-full input input-${
                            errors.confirmPassword && touched.confirmPassword
                              ? 'error'
                              : 'primary'
                          }`}
                        />
                        <button
                          type="button"
                          className={`btn btn-${
                            errors.confirmPassword && touched.confirmPassword
                              ? 'error'
                              : 'primary'
                          } join-item text-white text-xl`}
                          onClick={() =>
                            setShowConfirmPassword(!showConfirmPassword)
                          }
                        >
                          {showConfirmPassword ? <FaEyeSlash /> : <FaEye />}
                        </button>
                      </span>
                      {errors.confirmPassword && touched.confirmPassword && (
                        <p className="text-error px-2 text-sm ">
                          {errors.confirmPassword}
                        </p>
                      )}
                    </span>

                    <button
                      type="submit"
                      className="btn btn-primary w-full mt-8 text-xl text-white"
                    >
                      Sign in
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

export default RegisterPage;
