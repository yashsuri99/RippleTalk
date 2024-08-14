import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { ValidatedDateSelector } from '../../../../components/ValidatedInput/ValidatedDateSelector';
import { getMonths, getYears, getDays } from '../../../../utils/DateUtils';
import { AppDispatch, RootState } from '../../../../redux/Store';
import { updateRegister } from '../../../../redux/Slices/RegisterSlice';
import { validateDob } from '../../../../services/Validators';

export const RegisterDateInput: React.FC = () => {
	const dispatch: AppDispatch = useDispatch();
	const state = useSelector((state: RootState) => state.register);

	const [valid, setValid] = useState<boolean>(true);

	const updateState = (
		name: string,
		value: string | number | boolean
	): void => {
		dispatch(
			updateRegister({
				name,
				value,
			})
		);
		console.log('up');
	};

	useEffect(() => {
		console.log(state.dob);
		let { day, month, year } = state.dob;

		if (day && month && year) {
			setValid(validateDob({ month, day, year }));
		}
		console.log(validateDob({ month, day, year }));
		dispatch(updateRegister({ name: 'dobValid', value: valid }));
	}, [state.dob.day, state.dob.month, state.dob.year, state.dobValid, valid]);

	return (
		<div className='register-date'>
			<ValidatedDateSelector
				style={'validated-month'}
				valid={valid}
				name={'Month'}
				dropdown={getMonths}
				dispatcher={updateState}
			/>
			<ValidatedDateSelector
				style={'validated-day'}
				valid={valid}
				name={'Day'}
				dropdown={getDays}
				dispatcher={updateState}
			/>
			<ValidatedDateSelector
				style={'validated-year'}
				valid={valid}
				name={'Year'}
				dropdown={getYears}
				dispatcher={updateState}
			/>
		</div>
	);
};
