import React, { useState, useEffect } from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { RootState, AppDispatch } from '../../../../redux/Store';
import { incrementStep } from '../../../../redux/Slices/RegisterSlice';
import './RegisterFormOne.css';
import { RegisterDateInput } from '../RegisterDateInput/RegisterDateInput';
import { RegisterNameInput } from '../RegisterNameInput/RegisterNameInput';
import { RegisterEmailInput } from '../RegisterEmailInput/RegisterEmailInput';
import { StyledNextButton } from '../RegisterNextButton/RegisterNextButton';

interface FormOneState {
	firstName: string;
	lastName: string;
	email: string;
	dateOfBirth: string;
}

export const RegisterFormOne: React.FC = () => {
	const registerState = useSelector((state: RootState) => state.register);

	const dispatch: AppDispatch = useDispatch();

	const nextPage = () => {
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
				<RegisterNameInput />
				<RegisterEmailInput />
				<RegisterDateInput />
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
