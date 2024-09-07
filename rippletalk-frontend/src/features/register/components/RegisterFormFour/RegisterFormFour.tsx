import React, { useEffect, useState } from 'react';
import { Checkbox } from '../../../../components/Checkbox/Checkbox';
import { DropDown } from '../../../../components/Dropdown/Dropdown';
import { ValidatedTextInput } from '../../../../components/ValidatedInput/ValidatedTextInput';
import { countryCodeDropDown } from '../../../../utils/RegisterModalUtils';
import { validatePhone } from '../../../../services/Validators';
import { StyledNextButton } from '../RegisterNextButton/RegisterNextButton';
import { useDispatch, useSelector } from 'react-redux';
import { AppDispatch, RootState } from '../../../../redux/Store';
import {
	updateUserPhone,
	updateRegister,
} from '../../../../redux/Slices/RegisterSlice';
import './RegisterFormFour.css';

export const RegisterFormFour: React.FC = () => {
	const state = useSelector((state: RootState) => state.register);
	const dispatch: AppDispatch = useDispatch();

	const [phoneCode, setPhoneCode] = useState<string>('+91');
	const [phoneNumber, setPhoneNumber] = useState<string>('');
	const [validNumber, setValidNumber] = useState<boolean>(true);

	const changeCode = (e: React.ChangeEvent<HTMLSelectElement>) => {
		setPhoneCode(e.target.value.split(' ')[0]);
	};

	const changePhoneNumber = (e: React.ChangeEvent<HTMLInputElement>) => {
		setPhoneNumber(e.target.value);
		dispatch(
			updateRegister({
				name: 'phoneNumber',
				value: e.target.value,
			})
		);
	};

	const sendPhoneNumber = () => {
		dispatch(
			updateUserPhone({
				username: state.username,
				phone: phoneNumber,
			})
		);
	};

	useEffect(() => {
		if (phoneNumber) {
			setValidNumber(validatePhone(phoneNumber));
		}
	}, [phoneCode, phoneNumber]);

	return (
		<div className='reg-step-four-container'>
			<div className='reg-step-four-content'>
				<h1 className='reg-step-four-header'>Add a phone number</h1>
				<p className='reg-step-four-subhead reg-step-four-paragraph'>
					Enter the phone number you would like to associate with your
					RippleTalk account. You won't get a verification code sent here.
				</p>
				<div className='reg-step-four-input'>
					<DropDown
						content={countryCodeDropDown}
						change={changeCode}
						label={'Country Code'}
						defaultValue={'India +91'}
					/>
					<ValidatedTextInput
						valid={true}
						name={'phoneNumber'}
						label={'Your Phone Number'}
						changeValue={changePhoneNumber}
					/>
					{validNumber ? (
						<></>
					) : (
						<p className='reg-step-four-invalid'>
							Please enter a valid 10 digit number
						</p>
					)}
				</div>
				<div className='reg-step-four-check-group'>
					<p className='reg-step-four-paragraph'>
						Let people who have your phone number find and connect with you on
						RippleTalk. <span className='reg-step-four-link'>Learn More</span>
					</p>
					<Checkbox />
				</div>
				<div className='reg-step-four-check-group'>
					<p className='reg-step-four-paragraph'>
						Let RippleTalk use your phone number to personalize our services,
						including ads (if permitted by your Ads preferences). If you don't
						enable this RippleTalk will still use your phone number for purposes
						including account security, spam, fraud and abuse prevention.{' '}
						<span className='reg-step-four-link'>
							See our Privacy Policy for more information
						</span>
					</p>
					<Checkbox />
				</div>
			</div>
			<StyledNextButton
				disabled={phoneNumber && validNumber ? false : true}
				color={'black'}
				active={phoneNumber && validNumber ? true : false}
				onClick={sendPhoneNumber}
			>
				Update Number
			</StyledNextButton>
		</div>
	);
};
