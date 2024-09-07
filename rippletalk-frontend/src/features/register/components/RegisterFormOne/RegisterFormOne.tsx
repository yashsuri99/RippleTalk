import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { RootState, AppDispatch } from '../../../../redux/Store';
import {
	incrementStep,
	updateRegister,
} from '../../../../redux/Slices/RegisterSlice';
import './RegisterFormOne.css';
import { RegisterDateInput } from '../RegisterDateInput/RegisterDateInput';
import { RegisterNameInput } from '../RegisterNameInput/RegisterNameInput';
import { RegisterEmailInput } from '../RegisterEmailInput/RegisterEmailInput';
import { StyledNextButton } from '../RegisterNextButton/RegisterNextButton';

export const RegisterFormOne: React.FC = () => {
	const registerState = useSelector((state: RootState) => state.register);

	const dispatch: AppDispatch = useDispatch();

	const nextPage = () => {
		dispatch(
			updateRegister({
				name: 'error',
				value: false,
			})
		);
		dispatch(incrementStep());
	};

	const [buttonActive, setButtonActive] = useState<boolean>(false);

	useEffect(() => {
		if (
			registerState.dobValid &&
			registerState.emailValid &&
			registerState.firstNameValid &&
			registerState.lastNameValid
		) {
			setButtonActive(true);
		} else {
			setButtonActive(false);
		}
	}, [registerState]);

	return (
		<div className='reg-step-one-container'>
			<div className='reg-step-one-content'>
				<h1 className='reg-step-one-container'>Create your account</h1>
				<RegisterNameInput
					firstName={registerState.firstName}
					lastName={registerState.lastName}
				/>
				<RegisterEmailInput email={registerState.email} />
				<div className='reg-step-one-dob-disclaimer'>
					<p className='reg-step-one-dob-header'>Date of Birth</p>
					<span className='reg-step-one-dob-text'>
						This will not be show publically. Confirm your own age, even if this
						account is for a bussiness, pet or something else.
					</span>
				</div>
				<RegisterDateInput date={registerState.dob} />
			</div>
			<StyledNextButton
				disabled={!buttonActive}
				color={'black'}
				active={buttonActive}
				onClick={nextPage}
			>
				Next
			</StyledNextButton>
		</div>
	);
};
