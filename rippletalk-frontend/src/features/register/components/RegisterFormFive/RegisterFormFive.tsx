import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { ValidatedTextInput } from '../../../../components/ValidatedInput/ValidatedTextInput';
import { AppDispatch, RootState } from '../../../../redux/Store';
import { StyledNextButton } from '../RegisterNextButton/RegisterNextButton';
import './RegisterFormFive.css';
import {
	resendEmail,
	sendVerification,
} from '../../../../redux/Slices/RegisterSlice';

export const RegisterFormFive: React.FC = () => {
	const state = useSelector((state: RootState) => state.register);

	const dispatch: AppDispatch = useDispatch();

	const [code, setCode] = useState<string>('');

	const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setCode(e.target.value);
	};

	const resend = () => {
		dispatch(resendEmail(state.username));
	};

	const verify = () => {
		console.log('Sent verification');
		dispatch(
			sendVerification({
				username: state.username,
				code,
			})
		);
	};

	return (
		<div className='reg-step-five-container'>
			<div className='reg-step-five-content'>
				<h1 className='reg-step-five-header'>We sent you a code</h1>
				<p className='reg-step-five-paragraph'>
					Enter it below to verify {state.email}
				</p>
				<ValidatedTextInput
					valid={true}
					name={'code'}
					label={'Verification Code'}
					changeValue={handleChange}
				/>
				<p className='reg-step-five-message' onClick={resend}>
					Didn't recieve a email?
				</p>
			</div>
			<StyledNextButton
				active={code ? true : false}
				disabled={code ? false : true}
				color={'black'}
				onClick={verify}
			>
				Next
			</StyledNextButton>
		</div>
	);
};
