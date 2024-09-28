import React from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../../../redux/Store';
import { ValidatedDisplay } from '../../../../components/ValidatedInput/ValidatedDisplay';
import { stringifyDate } from '../../../../utils/DateUtils';
import './RegisterForms.css';
import '../../../../assets/global.css';

export const RegisterFormThree: React.FC = () => {
	const state = useSelector((state: RootState) => state.register);

	return (
		<div className='register-container'>
			<div className='register-content'>
				<h1 className='register-header'>Create your account</h1>
				<div className='register-three-value-wrapper'>
					<ValidatedDisplay
						label={'Name'}
						value={`${state.firstName} ${state.lastName}`}
					/>
				</div>
				<div
					className={
						state.error
							? 'register-three-bottom'
							: 'register-three-value-wrapper'
					}
				>
					<ValidatedDisplay label={'Email'} value={`${state.email}`} />
					{state.error ? (
						<p className='register-error color-red'>
							The email you specified is in use, please use a different one.
						</p>
					) : (
						<></>
					)}
				</div>
				<div className='register-three-value-wrapper'>
					<ValidatedDisplay
						label={'Birth Date'}
						value={stringifyDate(state.dob)}
					/>
				</div>
				<p className='register-text-sm color-gray'>
					By signing up you agree{' '}
					<span className='register-link color-blue'>Terms of Services</span>{' '}
					and
					<span className='register-link color-blue'>Privacy Policy</span>,
					inlcuding{' '}
					<span className='register-link color-blue'>Cookies Use</span>.
					RippleTalk may use your contact information, including your email
					address and phone number for purposes outlined in your Privacy Policy,
					like keeping your account secure and personalizing our services
					including ads.{' '}
					<span className='register-link color-blue'>Learn more</span>. Others
					will be able to find you by email or phone number, when provided
					unless you choose otherwise{' '}
					<span className='register-link color-blue'>here</span>
				</p>
			</div>
		</div>
	);
};
